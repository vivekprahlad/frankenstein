package com.thoughtworks.frankenstein.application;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.common.Constants;
import com.thoughtworks.frankenstein.events.DefaultEventRegistry;
import com.thoughtworks.frankenstein.events.EnterTextEvent;
import com.thoughtworks.frankenstein.recorders.ScriptListener;
import com.thoughtworks.frankenstein.script.Script;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Ensures behaviour of socket listener
 */
public class SocketListenerTest extends MockObjectTestCase {

    public void testAcceptsTextFromSocket() throws IOException, InterruptedException {
        MockFrankensteinRecorder recorder = new MockFrankensteinRecorder();
        SocketListener listener = new SocketListener(recorder);
        listener.start(Constants.LISTEN_PORT);
        Socket socket = new Socket(InetAddress.getLocalHost(), Constants.LISTEN_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("EnterText \"textField\" \"abc\"\n");
        writer.write("EnterText \"def\" \"def\"\n");
        writer.write(Constants.END_OF_SCRIPT + "\n");
        writer.flush();

        synchronized (MockFrankensteinRecorder.RECORDER_LOCK) {
            MockFrankensteinRecorder.RECORDER_LOCK.wait(10000);
        }

        assertNotNull(recorder.events);
        assertEquals(2, recorder.events.size());
        assertEquals(new EnterTextEvent("textField", "abc"), recorder.events.get(0));
        assertEquals(new EnterTextEvent("def", "def"), recorder.events.get(1));

        listener.scriptCompleted(true);

        String testStatus = reader.readLine();
        assertEquals(Constants.SCRIPT_PASSED, testStatus);

        String eof = reader.readLine();
        assertNull(eof);

        reader.close();
        writer.close();
        socket.close();
        listener.stop();
    }

    private static class MockFrankensteinRecorder implements FrankensteinRecorder {
        List events;
        static final Object RECORDER_LOCK = new Object();

        public void start() {
        }

        public void stop() {
        }

        public void play() {
            synchronized (RECORDER_LOCK) {
                RECORDER_LOCK.notifyAll();
            }
        }

        public void save(File file) throws IOException {
        }

        public void load(File selectedFile) throws IOException {
        }

        public void reset() {
        }

        public void registerAction(Class frankensteinEvent) {
        }

        public void registerRecorder(Class recorderClass) {
        }

        public void registerEvent(Class frankensteinEvent) {
        }

        public void load(Reader reader) throws IOException {
            events = new Script(new DefaultEventRegistry()).parse(reader);
        }

        public void addScriptListener(ScriptListener listener) {
        }

        public void removeScriptListener(ScriptListener listener) {
        }

        public void startRecording() {
        }

        public void stopRecording() {
        }


        public void addTestReporter(TestReporter reporter) {
        }

        public void removeAllTestReporters() {
        }
    }
}

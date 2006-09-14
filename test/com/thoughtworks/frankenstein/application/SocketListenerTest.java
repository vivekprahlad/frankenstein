package com.thoughtworks.frankenstein.application;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.events.DefaultEventRegistry;
import com.thoughtworks.frankenstein.events.EnterTextEvent;
import com.thoughtworks.frankenstein.script.Script;
import com.thoughtworks.frankenstein.recorders.ScriptListener;

/**
 * Ensures behaviour of socket listener
 */
public class SocketListenerTest extends MockObjectTestCase {

    public void testAcceptsTextFromSocket() throws IOException, InterruptedException {
        MockFrankensteinRecorder recorder = new MockFrankensteinRecorder();
        SocketListener listener = new SocketListener(recorder);
        listener.start();
        Socket socket = new Socket(InetAddress.getLocalHost(), 5678);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("EnterText \"textField\" \"abc\"\n");
        writer.write("EnterText \"def\" \"def\"\n");
        writer.flush();
        writer.close();
        socket.close();
        synchronized (MockFrankensteinRecorder.RECORDER_LOCK) {
            MockFrankensteinRecorder.RECORDER_LOCK.wait(10000);
        }
        assertNotNull(recorder.events);
        assertEquals(2, recorder.events.size());
        assertEquals(new EnterTextEvent("textField", "abc"), recorder.events.get(0));
        assertEquals(new EnterTextEvent("def", "def"), recorder.events.get(1));
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

        public void registerRecorder(Class recorderClass) {
        }

        public void registerEvent(Class frankensteinEvent) {
        }

        public void load(Reader reader) throws IOException {
            events = new Script(new DefaultEventRegistry()).parse(reader);
            reader.close();
        }

        public void addScriptListener(ScriptListener listener) {
        }

        public void removeScriptListener(ScriptListener listener) {
        }
    }
}

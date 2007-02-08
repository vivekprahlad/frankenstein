package com.thoughtworks.frankenstein.application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.common.DefaultComponentDecoder;
import com.thoughtworks.frankenstein.events.ActivateWindowEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.recorders.ComponentRecorderToTestFiringEvents;
import com.thoughtworks.frankenstein.recorders.Recorder;
import com.thoughtworks.frankenstein.recorders.ScriptListener;

/**
 * Ensures behaviour of frankenstein recorder.
 */
public class DefaultFrankensteinRecorderTest extends MockObjectTestCase {
    private Mock mockRecorder;
    private DefaultFrankensteinRecorder compositeRecorder;

    protected void setUp() throws Exception {
        super.setUp();
        mockRecorder = mock(Recorder.class);
        mockRecorder.expects(once()).method("stop");
        compositeRecorder = new DefaultFrankensteinRecorder((Recorder) mockRecorder.proxy(), new DefaultComponentDecoder(),
                new DefaultNamingStrategy());
    }

    public void testReset() {
        mockRecorder.expects(once()).method("reset");
        compositeRecorder.reset();
    }

    public void testStop() {
        mockRecorder.expects(once()).method("stop");
        compositeRecorder.stop();
    }

    public void testStart() {
        mockRecorder.expects(once()).method("start");
        compositeRecorder.start();
    }

    public void testPlay() {
        mockRecorder.expects(once()).method("play");
        compositeRecorder.play();
    }

    public void testSave() throws IOException {
        mockRecorder.expects(once()).method("eventList").will(returnValue(eventList()));
        File file = new File("testscript");
        compositeRecorder.save(file);
        assertTrue(file.exists());
        file.delete();
    }

    public void testLoad() throws IOException {
        mockRecorder.expects(once()).method("eventList").will(returnValue(eventList()));
        File file = new File("testscript");
        compositeRecorder.save(file);
        mockRecorder.expects(once()).method("setEventList").with(eq(eventList()));
        compositeRecorder.load(new FileReader(file));
        file.delete();
    }

    public void testEventIsFiredWhenStartRecordingIsCalled() {
        ComponentRecorderToTestFiringEvents.eventDispatchedCallCount = 0;
        compositeRecorder.registerRecorder(ComponentRecorderToTestFiringEvents.class);
        compositeRecorder.startRecording();
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JButton("Test"));
        compositeRecorder.stopRecording();
        assertTrue(ComponentRecorderToTestFiringEvents.eventDispatchedCallCount > 0);
    }

    public void testNoEventIsFiredWhenStartRecordingIsNotCalled() {
        ComponentRecorderToTestFiringEvents.eventDispatchedCallCount = 0;
        compositeRecorder.registerRecorder(ComponentRecorderToTestFiringEvents.class);
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JButton("Test"));
        assertEquals(0, ComponentRecorderToTestFiringEvents.eventDispatchedCallCount);
    }

    public void testRemoveScriptListener() {
        ScriptListener listener = new ScriptListener() {
            public void scriptCompleted(boolean passed) {
            }

            public void scriptStepStarted(int frankensteinEvent) {
            }

        };
        mockRecorder.expects(once()).method("removeScriptListener").with(same(listener));
        compositeRecorder.removeScriptListener(listener);
    }

    public void testCheckIfAllRecordersAreRegistered() {
        assertEquals(23, compositeRecorder.recorders.size());
    }

    private List eventList() {
        List eventList = new ArrayList();
        eventList.add(new ActivateWindowEvent("title"));
        return eventList;
    }
}

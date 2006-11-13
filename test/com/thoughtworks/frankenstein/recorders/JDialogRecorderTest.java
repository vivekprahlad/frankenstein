package com.thoughtworks.frankenstein.recorders;

import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.DialogShownEvent;
import com.thoughtworks.frankenstein.events.DialogClosedEvent;

/**
 * Ensures behaviour of the DialogRecorder.
 */
public class JDialogRecorderTest extends AbstractRecorderTestCase {
    private DialogRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        recorder = new DialogRecorder((Recorder) mockRecorder.proxy());
    }

    public void testAddsWindowListenerDuringRegistration() {
        int numberOfListeners = numberOfListeners();
        recorder.register();
        assertEquals(numberOfListeners + 1, numberOfListeners());
    }

    public void testRemovesWindowListenerDuringDeregistration() {
        int numberOfListeners = numberOfListeners();
        recorder.register();
        recorder.unregister();
        assertEquals(numberOfListeners, numberOfListeners());
    }

    private int numberOfListeners() {
        return Toolkit.getDefaultToolkit().getAWTEventListeners().length;
    }

    public void testDoesNotListenForEventsOtherThanJDialogs() {
        mockRecorder.expects(never()).method(ANYTHING).withAnyArguments();
        recorder.eventDispatched(new WindowEvent(new JFrame(), WindowEvent.WINDOW_OPENED));
    }

    public void testListensForDialogsShowing() {
        mockRecorder.expects(once()).method("record").with(eq(new DialogShownEvent("title")));
        recorder.eventDispatched(new WindowEvent(new JDialog(new JFrame(), "title"), WindowEvent.WINDOW_OPENED));
    }

    public void testRecordsDialogClosedEventWhenDialogIsHidden() {
        mockRecorder.expects(once()).method("record").with(eq(new DialogClosedEvent("title")));
        recorder.windowClosed(new WindowEvent(new JDialog(new JFrame(), "title"), WindowEvent.WINDOW_CLOSED));
    }
}

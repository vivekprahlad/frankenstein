package com.thoughtworks.frankenstein.recorders;

import java.awt.event.WindowEvent;
import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;
import com.thoughtworks.frankenstein.events.ActivateWindowEvent;
import com.thoughtworks.frankenstein.ui.RecorderPane;

/**
 * Ensures behaviour of the JInternalFrame.
 */
public class WindowActivationRecorderTest extends AbstractRecorderTestCase {
    private WindowActivationRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        recorder = new WindowActivationRecorder((Recorder) mockRecorder.proxy());
    }

    public void testListensForWindowsGettingActivated() {
        mockRecorder.expects(once()).method("record").with(eq(new ActivateWindowEvent("testTitle")));
        recorder.eventDispatched(new WindowEvent(new JFrame("testTitle"), WindowEvent.WINDOW_ACTIVATED));
    }

    public void testDoesNotRecordActivationOfRecorderFrame() {
        mockRecorder.expects(never()).method("record").with(ANYTHING);
        recorder.eventDispatched(new WindowEvent(createRecorderFrame(), WindowEvent.WINDOW_ACTIVATED));
    }

    private JFrame createRecorderFrame() {
        JFrame frame = new JFrame("testTitle");
        Mock mockFrankensteinRecorder = mock(FrankensteinRecorder.class);
        frame.getContentPane().add(new RecorderPane((FrankensteinRecorder) mockFrankensteinRecorder.proxy(), null, null));
        return frame;
    }

}

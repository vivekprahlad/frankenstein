package com.thoughtworks.frankenstein.recorders;

import java.awt.event.ComponentEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.InternalFrameShownEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the JInternalFrame.
 */
public class InternalFrameRecorderTest extends AbstractRecorderTestCase {
    private InternalFrameRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        recorder = new InternalFrameRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testDoesNotListenForEventsOtherThanJInternalFrames() {
        mockRecorder.expects(never()).method(ANYTHING).withAnyArguments();
        recorder.eventDispatched(new ComponentEvent(new JButton(), ComponentEvent.COMPONENT_SHOWN));
    }

    public void testListensForOptionPanesShowing() {
        mockRecorder.expects(once()).method("record").with(eq(new InternalFrameShownEvent("title")));
        recorder.componentShown(new JInternalFrame("title"));
    }

    public void testDoesNothingWhenInternalFrameIsHidden() {
        recorder.componentHidden(new JInternalFrame());
    }
}

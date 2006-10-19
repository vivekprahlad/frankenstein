package com.thoughtworks.frankenstein.recorders;

import java.awt.event.ComponentEvent;
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
        recorder = new DialogRecorder((Recorder) mockRecorder.proxy(), null);
    }

    public void testDoesNotListenForEventsOtherThanJDialogs() {
        mockRecorder.expects(never()).method(ANYTHING).withAnyArguments();
        recorder.eventDispatched(new ComponentEvent(new JButton(), ComponentEvent.COMPONENT_SHOWN));
    }

    public void testListensForDialogsShowing() {
        mockRecorder.expects(once()).method("record").with(eq(new DialogShownEvent("title")));
        recorder.componentShown(new JDialog(new JFrame(), "title"));
    }

    public void testRecordsDialogClosedEventWhenDialogIsHidden() {
        mockRecorder.expects(once()).method("record").with(eq(new DialogClosedEvent("title")));
        recorder.componentHidden(new JDialog(new JFrame(), "title"));
    }
}

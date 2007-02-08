package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.AssertLabelEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of AssertLabelRecorder
 */
public class AssertLabelRecorderTest extends AbstractRecorderTestCase {
    private AssertLabelRecorder recorder;
    private JLabel label;

    protected void setUp() throws Exception {
        super.setUp();
        label = new JLabel("labelValue");
        JScrollPane scrollPane = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        new JFrame().getContentPane().add(scrollPane);
        recorder = new AssertLabelRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsMouseListenerWhenListIsShown() {
        int listenerCount = listenerCount();
        recorder.componentShown(label);
        assertTrue(listenerCount() == listenerCount + 1);
    }

    public void testRemovesMouseListenerWhenListIsHidden() {
        int listenerCount = listenerCount();
        recorder.componentShown(label);
        recorder.componentHidden(label);
        assertTrue(listenerCount() == listenerCount);
    }

    public void testRecordsClickOnLabel() {
        recorder.componentShown(label);
        mockRecorder.expects(once()).method("record").with(eq(new AssertLabelEvent("label", "labelValue")));
        Point point = label.getLocation();
        recorder.listener.mouseClicked(new MouseEvent(label, MouseEvent.MOUSE_CLICKED, 0, 6, point.x, point.y, 1, false, MouseEvent.BUTTON3));
    }


    private int listenerCount() {
        return label.getMouseListeners().length;
    }
}

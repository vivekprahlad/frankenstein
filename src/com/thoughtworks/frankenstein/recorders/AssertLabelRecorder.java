package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.AssertLabelEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 */
public class AssertLabelRecorder extends AbstractComponentRecorder {
    protected MouseListener listener = new MouseListener();

    public AssertLabelRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JLabel.class);
    }

    void componentShown(Component component) {
        label(component).addMouseListener(listener);
    }

    private JLabel label(Component component) {
        return ((JLabel) component);
    }

    void componentHidden(Component component) {
        label(component).removeMouseListener(listener);
    }


    private void recordAssertEvent(MouseEvent e) {
        if ((e.getButton() == MouseEvent.BUTTON3) && (e.getModifiers() == (6))) {
            JLabel label = (JLabel) e.getSource();
            recorder.record(new AssertLabelEvent("label", label.getText()));
        }
    }

    protected class MouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            recordAssertEvent(e);
        }
    }
}

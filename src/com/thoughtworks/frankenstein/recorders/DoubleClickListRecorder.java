package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.events.DoubleClickListEvent;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 Understands doubleclick on lists
 */
public class DoubleClickListRecorder extends AbstractComponentRecorder implements MouseListener {

    public DoubleClickListRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JList.class);
    }

    void componentShown(Component component) {
        MouseListener[] listeners = list(component).getMouseListeners();
        removeAllMouseListeners(listeners, list(component));
        list(component).addMouseListener(this);
        addAllMouseListeners(listeners, list(component));
    }

    private void addAllMouseListeners(MouseListener[] listeners, JList list) {
        for (int i = 0; i < listeners.length; i++) {
            list.addMouseListener(listeners[i]);
        }
    }

    private void removeAllMouseListeners(MouseListener[] listeners, JList list) {
        for (int i = 0; i < listeners.length; i++) {
            list.removeMouseListener(listeners[i]);
        }
    }

    private JList list(Component component) {
        return ((JList) component);
    }

    void componentHidden(Component component) {
        list(component).removeMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            JList list = (JList) e.getSource();
            recorder.record(new DoubleClickListEvent(list.getName(), list.locationToIndex(e.getPoint())));
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}

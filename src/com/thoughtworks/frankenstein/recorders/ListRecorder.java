package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.ListEvent;
import com.thoughtworks.frankenstein.events.actions.Action;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Records double and right clicks on lists
 */
public class ListRecorder extends AbstractComponentRecorder {
    protected MouseListener listMouseListener = new ListMouseListener();

    public ListRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JList.class);
    }

    void componentShown(Component component) {
        MouseListener[] listeners = list(component).getMouseListeners();
        removeAllMouseListeners(listeners, list(component));
        list(component).addMouseListener(listMouseListener);
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
        list(component).removeMouseListener(listMouseListener);
    }

    private void record(MouseEvent e, Action action) {
        JList list = (JList) e.getSource();
        recorder.record(new ListEvent(list.getName(), list.locationToIndex(e.getPoint()), action));
    }

    private void recordRightClick(MouseEvent e) {
        if (e.isPopupTrigger()) {
            record(e, new RightClickAction());
        }
    }

    private void recordDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            record(e, new DoubleClickAction());
        }
    }
    private class ListMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            recordDoubleClick(e);
            recordRightClick(e);
        }

        public void mousePressed(MouseEvent e) {
            recordRightClick(e);
        }

        public void mouseReleased(MouseEvent e) {
            recordRightClick(e);
        }
    }
}

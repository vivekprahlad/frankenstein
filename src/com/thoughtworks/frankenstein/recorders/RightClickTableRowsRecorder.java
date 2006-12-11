package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.events.TableRowEvent;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

/**
 * Ensures RightClcikTableRowsRecorder works as expected
 */
public class RightClickTableRowsRecorder extends AbstractComponentRecorder {
    private MouseListener tableListener = new TableMouseListener();

    public RightClickTableRowsRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JTable.class);
    }

    void componentShown(Component component) {
        MouseListener[] listeners = table(component).getMouseListeners();
        removeAllMouseListeners(listeners, table(component));
        table(component).addMouseListener(tableListener);
        addAllMouseListeners(listeners, table(component));
    }

    private void addAllMouseListeners(MouseListener[] listeners, JTable table) {
        for (int i = 0; i < listeners.length; i++) {
            table.addMouseListener(listeners[i]);
        }
    }

    private void removeAllMouseListeners(MouseListener[] listeners, JTable table) {
        for (int i = 0; i < listeners.length; i++) {
            table.removeMouseListener(listeners[i]);
        }
    }

    private JTable table(Component component) {
        return ((JTable) component);
    }

    void componentHidden(Component component) {
        table(component).removeMouseListener(tableListener);
    }

    public void mouseClicked(MouseEvent e) {
        recordTableRowEvent(e);
    }

    private void recordTableRowEvent(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            JTable table = (JTable) e.getSource();
            int rowIndex = table.rowAtPoint(e.getPoint());
            recorder.record(new TableRowEvent(componentName(table), rowIndex, new RightClickAction()));
        }
    }

    private class TableMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            recordTableRowEvent(e);
        }
    }
}

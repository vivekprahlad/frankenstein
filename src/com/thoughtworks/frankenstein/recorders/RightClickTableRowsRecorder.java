package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.events.TableRowEvent;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * Ensures RightClcikTableRowsRecorder works as expected
 */
public class RightClickTableRowsRecorder extends AbstractComponentRecorder implements MouseListener {
    public RightClickTableRowsRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JTable.class);
    }

    void componentShown(Component component) {
        MouseListener[] listeners = table(component).getMouseListeners();
        removeAllMouseListeners(listeners, table(component));
        table(component).addMouseListener(this);
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
        table(component).removeMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            JTable table = (JTable) e.getSource();
            int rowIndex = table.rowAtPoint(e.getPoint());
            recorder.record(new TableRowEvent(componentName(table), rowIndex, new RightClickAction()));
        }
    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}

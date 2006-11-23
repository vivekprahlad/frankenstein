package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.events.ClickTableHeaderEvent;
import com.thoughtworks.frankenstein.common.ComponentName;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cprakash
 * Date: Nov 21, 2006
 * Time: 11:11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClickTableHeaderRecorder extends AbstractComponentRecorder implements MouseListener {
    public ClickTableHeaderRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JTableHeader.class);
    }

    void componentShown(Component component) {
        MouseListener[] listeners = header(component).getMouseListeners();
        removeAllMouseListeners(listeners, header(component));
        header(component).addMouseListener(this);
        addAllMouseListeners(listeners, header(component));
    }

    private JTableHeader header(Component component) {
        return ((JTableHeader) component);
    }

    private void addAllMouseListeners(MouseListener[] listeners, JTableHeader header) {
        for (int i = 0; i < listeners.length; i++) {
            header.addMouseListener(listeners[i]);
        }
    }

    private void removeAllMouseListeners(MouseListener[] listeners, JTableHeader header) {
        for (int i = 0; i < listeners.length; i++) {
            header.removeMouseListener(listeners[i]);
        }
    }

    void componentHidden(Component component) {
        header(component).removeMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            JTableHeader header = (JTableHeader) e.getSource();
            int columnIndex = header.columnAtPoint(e.getPoint());
            TableColumn column = header.getColumnModel().getColumn(columnIndex);
            recorder.record(new ClickTableHeaderEvent(componentName(header), (String) column.getHeaderValue()));
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

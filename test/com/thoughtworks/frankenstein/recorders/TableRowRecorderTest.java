package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.events.TableRowEvent;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Ensures behaviour of TableRowRecorder
 */
public class TableRowRecorderTest extends AbstractRecorderTestCase {
    private JTable table;
    private TableRowRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        table = new JTable(3, 3);
        table.setName("table");
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        new JFrame().getContentPane().add(scrollPane);
        recorder = new TableRowRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsMouseListenerWhenListIsShown() {
        int listenerCount = listenerCount();
        recorder.componentShown(table);
        assertTrue(listenerCount() == listenerCount + 1);
    }

    public void testRemovesMouseListenerWhenListIsHidden() {
        int listenerCount = listenerCount();
        recorder.componentShown(table);
        recorder.componentHidden(table);
        assertTrue(listenerCount() == listenerCount);
    }

     public void testRecordsClickOnFirstRow() {
        recorder.componentShown(table);
        mockRecorder.expects(once()).method("record").with(eq(new TableRowEvent("table",0, new RightClickAction())));
        Point point = locationOfFirstRow();
        recorder.recordTableRowEvent(new MouseEvent(table, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 1, true,MouseEvent.BUTTON3));
    }

    public void testRecordsDoubleClickOnFirstRow() {
        recorder.componentShown(table);
        mockRecorder.expects(once()).method("record").with(eq(new TableRowEvent("table",0, new DoubleClickAction())));
        Point point = locationOfFirstRow();
        recorder.recordTableRowEvent(new MouseEvent(table, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 2, false,MouseEvent.BUTTON1));
    }

    private Point locationOfFirstRow() {
        Point point=table.getLocation();
        point.y+=(table.getRowHeight(0))/2;
        point.x+=(table.getWidth())/2;
        return point;

    }

    private int listenerCount() {
        return table.getMouseListeners().length;
    }


}

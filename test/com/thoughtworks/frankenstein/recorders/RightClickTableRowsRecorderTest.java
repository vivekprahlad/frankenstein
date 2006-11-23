package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.events.RightClickTableRowsEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**

 */
public class RightClickTableRowsRecorderTest extends AbstractRecorderTestCase {
    private JTable table;
    private RightClickTableRowsRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        table = new JTable(3, 3);
        table.setName("table");
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        new JFrame().getContentPane().add(scrollPane);
        recorder = new RightClickTableRowsRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
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

     public void testRecordsClickOnTableHeader() {
        recorder.componentShown(table);
        mockRecorder.expects(once()).method("record").with(eq(new RightClickTableRowsEvent("table",0)));
        Point point = LocationForFirstRow();
        recorder.mouseClicked(new MouseEvent(table, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 1, false,MouseEvent.BUTTON3));
    }

    private Point LocationForFirstRow() {
        Point point=table.getLocation();
        point.y+=(table.getRowHeight(0))/2;
        point.x+=(table.getWidth())/2;
        return point;

    }

    private int listenerCount() {
        return table.getMouseListeners().length;
    }


}

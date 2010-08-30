package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.thoughtworks.frankenstein.events.TableHeaderEvent;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 */
public class ClickTableHeaderRecorderTest extends AbstractRecorderTestCase {
    private JTable table;
    private JTableHeader header;
    private ClickTableHeaderRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        table = new JTable(3, 3);
        header = table.getTableHeader();
        header.setName("header");
        header.getColumnModel().getColumn(0).setHeaderValue("one");
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        new JFrame().getContentPane().add(scrollPane);
        recorder = new ClickTableHeaderRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsMouseListenerWhenListIsShown() {
        int listenerCount = listenerCount();
        recorder.componentShown(header);
        assertTrue(listenerCount() == listenerCount + 1);
    }

    public void testRemovesMouseListenerWhenListIsHidden() {
        int listenerCount = listenerCount();
        recorder.componentShown(header);
        recorder.componentHidden(header);
        assertTrue(listenerCount() == listenerCount);
    }

    public void testRecordsClickOnTableHeader() {
        recorder.componentShown(header);
        mockRecorder.expects(once()).method("record").with(eq(new TableHeaderEvent("header", "one", new ClickAction())));
        Point point = LocationForFirstColumn();
        recorder.mouseClicked(new MouseEvent(header, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 1, false, MouseEvent.BUTTON1));
    }

    private Point LocationForFirstColumn() {
        Point point = header.getLocation();
        point.x += (header.getColumnModel().getColumn(0).getWidth()) / 2;
        point.y += (header.getHeight()) / 2;
        return point;
    }

    private int listenerCount() {
        return header.getMouseListeners().length;
    }
}

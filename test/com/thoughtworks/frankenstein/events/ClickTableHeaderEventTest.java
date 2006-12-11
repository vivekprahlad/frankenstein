package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 */
public class ClickTableHeaderEventTest extends AbstractEventTestCase {


    public void testEqualsAndHashCode() {
        ClickTableHeaderEvent eventOne = new ClickTableHeaderEvent("header", "one");
        ClickTableHeaderEvent eventTwo = new ClickTableHeaderEvent("header", "one");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("ClickTableHeaderEvent: header one", new ClickTableHeaderEvent("header", "one").toString());
    }

    public void testAction() {
        assertEquals("ClickTableHeader", new ClickTableHeaderEvent("header", "one").action());
    }

    public void testTarget() {
        assertEquals("header", new ClickTableHeaderEvent("header", "one").target());
    }

    public void testParameters() {
        assertEquals("one", new ClickTableHeaderEvent("header", "one").parameters());
    }

    public void testScriptLine() {
        assertEquals("click_table_header \"header\" , \"one\"", new ClickTableHeaderEvent("header", "one").scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JTable table = new JTable(3, 3);
        JFrame frame = new JFrame();
        frame.getContentPane().add(table);
        frame.pack();
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setName("header");
        tableHeader.getColumnModel().getColumn(0).setHeaderValue("one");
        tableHeader.getColumnModel().getColumn(1).setHeaderValue("two");
        ClickTableHeaderEvent event = new ClickTableHeaderEvent("header", "two");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        MockMouseListener mockMouseListener = new MockMouseListener();
        tableHeader.addMouseListener(mockMouseListener);
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("header")).will(returnValue(tableHeader));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
        assertTrue(mockMouseListener.singleClicked);
        assertFalse(mockMouseListener.popupTrigger);
        assertEquals(1, tableHeader.columnAtPoint(mockMouseListener.point));
    }

    protected FrankensteinEvent createEvent() {
        return new ClickTableHeaderEvent("header", "one");
    }

    private class MockMouseListener extends MouseAdapter {
        private boolean singleClicked = false;
        private boolean popupTrigger = true;
        private Point point;
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1&&(e.getButton()==MouseEvent.BUTTON1)) {
                singleClicked = true;
            }
            popupTrigger = e.isPopupTrigger();
            point = e.getPoint();
        }
    }
}

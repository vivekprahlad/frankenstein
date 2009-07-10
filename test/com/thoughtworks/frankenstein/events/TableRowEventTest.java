package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.common.RobotFactory;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;

/**
 * Ensures TableRowEvent works as expected
 */
public class TableRowEventTest extends AbstractEventTestCase {
    private TableRowEventTest.MockMouseListener mockMouseListener;

    public void testEqualsAndHashCode() {
        TableRowEvent eventOne = createTableRowEvent();
        TableRowEvent eventTwo = createTableRowEvent();
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    private TableRowEvent createTableRowEvent() {
        return new TableRowEvent("table", 1, new RightClickAction());
    }

    public void testToString() {
        assertEquals("RightClickTableRowEvent: table 1", createTableRowEvent().toString());
    }

    public void testAction() {
        assertEquals("RightClickTableRow", createTableRowEvent().action());
    }

    public void testTarget() {
        assertEquals("table", createTableRowEvent().target());
    }

    public void testParameters() {
        assertEquals("1", createTableRowEvent().parameters());
    }

    public void testScriptLine() {
        assertEquals("right_click_table_row \"table\" , \"1\"", createTableRowEvent().scriptLine());
    }

    public void testScriptLineInJava() {
        assertEquals("rightClickTableRow(\"table\" , 1)", createTableRowEvent().scriptLine(new JavaScriptStrategy()));
    }

    public void testPlaysEvent() throws Exception {
        doTestAtRow(2);
    }

    private void doTestAtRow(int row) {
        JTable table = new JTable(3, 3);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        table.addMouseListener(mockMouseListener);
        TableRowEvent event = new TableRowEvent("table", row, new DoubleClickAction());
        mockComponentFinder.expects(once()).method("findComponent").with(ANYTHING, eq("table")).will(returnValue(table));
        event.play(new DefaultWindowContext(), (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
        assertEquals(row, table.rowAtPoint(mockMouseListener.point));
    }

    public void testPlaysActionOnZerothRow() {
        doTestAtRow(0);
    }

    protected FrankensteinEvent createEvent() {
        return createTableRowEvent();
    }

    protected void setUp() throws Exception {
        super.setUp();
        mockMouseListener = new MockMouseListener();
    }

    private class MockMouseListener extends MouseAdapter {
        private Point point;

        public void mouseClicked(MouseEvent e) {
            point = e.getPoint();
        }
    }
}

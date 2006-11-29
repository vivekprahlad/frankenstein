package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;


import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;
import com.thoughtworks.frankenstein.events.actions.*;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.*;

/**
 * Ensures TableRowEvent works as expected
 */
public class TableRowEventTest extends AbstractEventTestCase {

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

    public void testPlaysEvent() throws Exception {
        JTable table = new JTable(3, 3);
        table.setRowHeight(16);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        Mock mockAction = mock(com.thoughtworks.frankenstein.events.actions.Action.class);
        mockAction.expects(once()).method("execute").with(eq(new Point(0, 40)), ANYTHING, ANYTHING);
        assertEquals(2, table.rowAtPoint(new Point(0, 40)));
        TableRowEvent event = new TableRowEvent("table", 2, (com.thoughtworks.frankenstein.events.actions.Action) mockAction.proxy());
        WindowContext context = (WindowContext) mockContext.proxy();
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("table")).will(returnValue(table));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
    }

    public void testPlaysActionOnZerothRow() {
        JTable table = new JTable(3, 3);
        table.setRowHeight(16);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        Mock mockAction = mock(com.thoughtworks.frankenstein.events.actions.Action.class);
        mockAction.expects(once()).method("execute").with(eq(new Point(0, 8)), ANYTHING, ANYTHING);
        assertEquals(0, table.rowAtPoint(new Point(0, 8)));
        TableRowEvent event = new TableRowEvent("table", 0, (com.thoughtworks.frankenstein.events.actions.Action) mockAction.proxy());
        WindowContext context = (WindowContext) mockContext.proxy();
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("table")).will(returnValue(table));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
    }

    protected FrankensteinEvent createEvent() {
        return createTableRowEvent();
    }
}

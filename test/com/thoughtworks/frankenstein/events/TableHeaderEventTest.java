package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;
import com.thoughtworks.frankenstein.events.actions.ClickAction;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

/**

 */
public class TableHeaderEventTest extends AbstractEventTestCase {


    public void testEqualsAndHashCode() {
        TableHeaderEvent eventOne = new TableHeaderEvent("header", "one", new ClickAction());
        TableHeaderEvent eventTwo = new TableHeaderEvent("header", "one", new ClickAction());
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("TableHeaderEvent: header one", new TableHeaderEvent("header", "one", new ClickAction()).toString());
    }

    public void testAction() {
        assertEquals("ClickTableHeader", new TableHeaderEvent("header", "one", new ClickAction()).action());
    }

    public void testTarget() {
        assertEquals("header", new TableHeaderEvent("header", "one", new ClickAction()).target());
    }

    public void testParameters() {
        assertEquals("one", new TableHeaderEvent("header", "one", new ClickAction()).parameters());
    }

    public void testScriptLine() {
        assertEquals("click_table_header \"header\" , \"one\"", new TableHeaderEvent("header", "one", new ClickAction()).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JTable table = new JTable(3, 3);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setName("header");
        tableHeader.getColumnModel().getColumn(0).setHeaderValue("one");
        tableHeader.getColumnModel().getColumn(1).setHeaderValue("two");
        Mock mockFinder = mock(ComponentFinder.class);
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("header")).will(returnValue(tableHeader));
        Mock mockAction = mock(com.thoughtworks.frankenstein.events.actions.Action.class);
        mockAction.expects(once()).method("execute").with(eq(new Point(112,0)), ANYTHING, ANYTHING, ANYTHING);
        assertSame(1,tableHeader.columnAtPoint(new Point(112,0)));
        new TableHeaderEvent("header", "two",(com.thoughtworks.frankenstein.events.actions.Action) mockAction.proxy()).play(null, (ComponentFinder) mockFinder.proxy(), null, null);
    }

    protected FrankensteinEvent createEvent() {
        return new TableHeaderEvent("header", "one", new ClickAction());
    }


}

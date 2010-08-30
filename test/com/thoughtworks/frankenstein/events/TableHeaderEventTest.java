package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.events.actions.MockAction;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

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

    public void testScriptLineInJava() {
        assertEquals("clickTableHeader(\"header\" , \"one\")", new TableHeaderEvent("header", "one", new ClickAction()).scriptLine(new JavaScriptStrategy()));
    }

    public void testPlaysEvent() throws Exception {
        JTable table = new JTable(3, 3);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setName("header");
        tableHeader.getColumnModel().getColumn(0).setHeaderValue("one");
        tableHeader.getColumnModel().getColumn(1).setHeaderValue("two");
        Mock mockFinder = mock(ComponentFinder.class);
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("header")).will(returnValue(tableHeader));
        MockAction mockAction = new com.thoughtworks.frankenstein.events.actions.MockAction();
        new TableHeaderEvent("header", "two", mockAction).play(null, (ComponentFinder) mockFinder.proxy(), null, null);
        assertEquals(1, tableHeader.columnAtPoint(mockAction.point));
    }

    protected FrankensteinEvent createEvent() {
        return new TableHeaderEvent("header", "one", new ClickAction());
    }


}

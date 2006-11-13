package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;

/**
 * Ensures the behaviour of the AssertTableRowsEvent.
 */
public class AssertTableRowsEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        AssertTableRowsEvent one = new AssertTableRowsEvent("table", 1);
        AssertTableRowsEvent two = new AssertTableRowsEvent("table", 1);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
       assertEquals("AssertTableRowsEvent: table, 1", new AssertTableRowsEvent("table", 1).toString());
    }

    public void testAction() {
        assertEquals("AssertTableRows", new AssertTableRowsEvent("table", 1).action());
    }

    public void testTarget() {
        assertEquals("table", new AssertTableRowsEvent("table", 1).target());
    }

    public void testParameters() {
        assertEquals("1", new AssertTableRowsEvent("table", 1).parameters());
    }

    public void testScriptLine() {
        assertEquals("assert_table_rows \"table\" , \"1\"", new AssertTableRowsEvent("table", 1).scriptLine());
    }

    public void testPlay() throws Exception {
        FrankensteinEvent event = new AssertTableRowsEvent("table", 1);
        Mock mockFinder = mock(ComponentFinder.class);
        JTable tableComponent = new JTable(1, 1);
        tableComponent.setName("table");
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("table")).will(returnValue(tableComponent));
        event.play(null, (ComponentFinder) mockFinder.proxy(), null, null);
    }

    protected FrankensteinEvent createEvent() {
        return new AssertTableRowsEvent("table", 1);
    }

    public void testPlayWithNumberOfRowsNotMatching() {
        FrankensteinEvent event = new AssertTableRowsEvent("table", 1);
        Mock mockFinder = mock(ComponentFinder.class);
        JTable tableComponent = new JTable(2, 1);
        tableComponent.setName("table");
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("table")).will(returnValue(tableComponent));
        try {
            event.play(null, (ComponentFinder) mockFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }
}


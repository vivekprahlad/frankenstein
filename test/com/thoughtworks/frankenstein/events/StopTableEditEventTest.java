package com.thoughtworks.frankenstein.events;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;

/**
 * Ensures behaviour of StopTableEditEvent
 */
public class StopTableEditEventTest extends MockObjectTestCase {

    public void testEqualsAndHashCode() {
        StopTableEditEvent eventOne = new StopTableEditEvent("parent.tableName");
        StopTableEditEvent eventTwo = new StopTableEditEvent("parent.tableName");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
        assertFalse(eventOne.equals(null));
    }

    public void testToString() {
        assertEquals("StopTableEditEvent: Table: parent.tableName", new StopTableEditEvent("parent.tableName").toString());
    }

    public void testAction() {
        assertEquals("StopTableEdit", new StopTableEditEvent("testTarget").action());
    }

    public void testTarget() {
        assertEquals("testTarget", new StopTableEditEvent("testTarget").target());
    }

    public void testParameters() {
        assertEquals("", new StopTableEditEvent("testTarget").parameters());
    }

    public void testScriptLine() {
        assertEquals("stop_table_edit \"testTarget\"", new StopTableEditEvent("testTarget").scriptLine());
    }

    public void testPlay() {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("setTableCellEditor").with(eq(null));
        mockComponentFinder.expects(once()).method("findComponent").will(returnValue(new JTable()));
        new StopTableEditEvent("testTarget").play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
    }
}

package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Ensures behaviour of SelectTableRowEvent
 *
 * @author vivek
 */
public class SelectTableRowEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        SelectTableRowEvent one = new SelectTableRowEvent("table", new int[]{1, 3, 8});
        SelectTableRowEvent two = new SelectTableRowEvent("table", new int[]{1, 3, 8});
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("SelectTableRowEvent: table {1,3,8}", new SelectTableRowEvent("table", new int[]{1, 3, 8}).toString());
    }

    public void testAction() {
        assertEquals("SelectTableRow", new SelectTableRowEvent("table", new int[]{1, 3, 8}).action());
    }

    public void testTarget() {
        assertEquals("table", new SelectTableRowEvent("table", new int[]{1, 3, 8}).target());
    }

    public void testParameters() {
        assertEquals("1,3,8", new SelectTableRowEvent("table", new int[]{1, 3, 8}).parameters());
    }

    public void testScriptLine() {
        assertEquals("select_table_row \"table\" , \"1,3,8\"", new SelectTableRowEvent("table", new int[]{1, 3, 8}).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JTable table = new JTable();
        SelectTableRowEvent event = new SelectTableRowEvent("table", new int[]{1, 2, 4});
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findComponent").will(returnValue(table));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
        waitForIdle();
        assertRows(new int[]{1, 2, 4}, table.getSelectedRows());
    }

    private void assertRows(int[] rows, int[] selectedRows) {
        assertEquals(rows.length, selectedRows.length);
        for (int i = 0; i < rows.length; i++) {
            assertEquals(rows[i], selectedRows[i]);
        }
    }

    private void assertListSelection(ListSelectionEvent event, int startIndex, int endIndex) {
        assertEquals(startIndex, event.getFirstIndex());
        assertEquals(endIndex, event.getLastIndex());
    }

    protected FrankensteinEvent createEvent() {
        return new SelectTableRowEvent("table", new int[]{1, 3, 8});
    }
}

package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.TestTableModel;

/**
 * Ensures behaviour of ClickButtonEvent
 */
public class CancelTableEditEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        CancelTableEditEvent eventOne = new CancelTableEditEvent("parent.tableName");
        CancelTableEditEvent eventTwo = new CancelTableEditEvent("parent.tableName");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
        assertFalse(eventOne.equals(null));
    }

    public void testToString() {
        assertEquals("CancelTableEditEvent: Table: parent.tableName", new CancelTableEditEvent("parent.tableName").toString());
    }

    public void testAction() {
        assertEquals("CancelTableEdit", new CancelTableEditEvent("testTarget").action());
    }

    public void testTarget() {
        assertEquals("testTarget", new CancelTableEditEvent("testTarget").target());
    }

    public void testParameters() {
        assertEquals("", new CancelTableEditEvent("testTarget").parameters());
    }

    public void testScriptLine() {
        assertEquals("cancel_table_edit \"testTarget\"", new CancelTableEditEvent("testTarget").scriptLine());
    }

    public void testPlaysEvent() {
        CancelTableEditEvent event = new CancelTableEditEvent("parent.tableName");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        final JTable table = new JTable(new TestTableModel());
        editTable(table);
        waitForIdle();
        assertTrue(table.isEditing());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.tableName")).will(returnValue(table));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        waitForIdle();
        assertFalse(table.isEditing());
    }

    protected FrankensteinEvent createEvent() {
        return new CancelTableEditEvent("tableName");
    }

    private void editTable(final JTable table) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                table.editCellAt(1, 1);
            }
        });
    }
}

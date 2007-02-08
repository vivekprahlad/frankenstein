package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;

import com.thoughtworks.frankenstein.events.SelectTableRowEvent;

/**
 * Ensures behaviour of SelectTableRecorder
 * @author vivek
 */
public class SelectTableRecorderTest extends AbstractRecorderTestCase {
    private SelectTableRecorder recorder;

    private JTable table;

    protected void setUp() throws Exception {
        super.setUp();
        recorder = new SelectTableRecorder((EventRecorder) mockRecorder.proxy(),null);
        table = new JTable();
        table.setName("table");
    }

    public void testAddsListSelectionListenerWhenTableIsShown() {
        int initialListenerCount = listSelectionListenerCount(table);
        recorder.componentShown(table);
        assertEquals(initialListenerCount + 1, listSelectionListenerCount(table));
    }

    public void testRemovesListSelectionListenerWhenTableIsHidden() {
        int initialListenerCount = listSelectionListenerCount(table);
        recorder.componentShown(table);
        recorder.componentHidden(table);
        assertEquals(initialListenerCount, listSelectionListenerCount(table));
    }

    public void testAddsColumnSelectionListenerWhenTableIsShown() {
        int initialListenerCount = columnSelectionListenerCount(table);
        recorder.componentShown(table);
        assertEquals(initialListenerCount + 1, columnSelectionListenerCount(table));
    }

    public void testRemovesColumnSelectionListenerWhenTableIsHidden() {
        int initialListenerCount = columnSelectionListenerCount(table);
        recorder.componentShown(table);
        recorder.componentHidden(table);
        assertEquals(initialListenerCount, columnSelectionListenerCount(table));
    }

    public void testRecordsSelectingOneRow() {
        recorder.componentShown(table);
        mockRecorder.expects(once()).method("record").with(eq(new SelectTableRowEvent("table", new int[] {0})));
        table.getSelectionModel().setSelectionInterval(0,0);
    }

    public void testRecordsSelectingOneRowAfterSelectionModelChanges() {
        recorder.componentShown(table);
        table.setSelectionModel(new DefaultListSelectionModel());
        mockRecorder.expects(once()).method("record").with(eq(new SelectTableRowEvent("table", new int[] {0})));
        table.getSelectionModel().setSelectionInterval(0,0);
    }

    public void testRecordsSelectingMultipleRowsAfterSelectionModelChanges() {
        recorder.componentShown(table);
        table.setSelectionModel(new DefaultListSelectionModel());
        mockRecorder.expects(once()).method("record").with(eq(new SelectTableRowEvent("table", new int[] {0})));
        mockRecorder.expects(once()).method("record").with(eq(new SelectTableRowEvent("table", new int[] {0,2,3,4})));
        table.getSelectionModel().setSelectionInterval(0,0);
        table.getSelectionModel().addSelectionInterval(2,4);
    }

    private int listSelectionListenerCount(JTable table) {
        return ((DefaultListSelectionModel) table.getSelectionModel()).getListSelectionListeners().length;
    }

    private int columnSelectionListenerCount(JTable table) {
        return ((DefaultTableColumnModel) table.getColumnModel()).getColumnModelListeners().length;
    }

}

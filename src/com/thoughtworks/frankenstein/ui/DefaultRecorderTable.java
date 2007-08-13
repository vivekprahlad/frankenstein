package com.thoughtworks.frankenstein.ui;

import javax.swing.*;

/**
 * Table for displaying the list of script events.
 *
 * @author Prakash and Pavan
 */
public class DefaultRecorderTable extends JTable implements RecorderTable {

    public DefaultRecorderTable(RecorderTableModel tableModel) {
        super(tableModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
    }

    public void selectRow(int indexOfRowToBeSelected) {
        setRowSelectionInterval(indexOfRowToBeSelected, indexOfRowToBeSelected);
        scrollRectToVisible(getCellRect(indexOfRowToBeSelected, 0, true));
    }

    public void selectLastRow() {
        if (getRowCount()>0) {
            selectRow(getRowCount()-1);
        }
    }
}

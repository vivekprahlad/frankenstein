package com.thoughtworks.frankenstein.ui;

/**
 * UI elements expected in tests for a table.
 */

public interface RecorderTable {
    void selectRow(int indexOfRowToBeSelected);

    void clearSelection();
}

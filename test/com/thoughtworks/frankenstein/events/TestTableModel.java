package com.thoughtworks.frankenstein.events;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Test Table model
 */
public class TestTableModel extends AbstractTableModel {
    private List values;

    public TestTableModel() {
        values = new ArrayList();
        values.add(new TestTableRow(Boolean.FALSE, "xyz"));
    }

    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
        }
        return null;
    }

    public int getRowCount() {
        return 1;
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TestTableRow row = (TestTableRow) values.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.value1;
            case 1:
                return row.value2;
        }
        return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TestTableRow row = (TestTableRow) values.get(rowIndex);
        switch (columnIndex) {
            case 0:
                row.value1 = (Boolean) aValue;
                return;
            case 1:
                row.value2 = (String) aValue;
                return;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}

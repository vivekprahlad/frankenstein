package com.thoughtworks.frankenstein.recorders;

import javax.swing.table.AbstractTableModel;
import java.util.Map;
import java.util.HashMap;
import java.awt.*;

/**
 * Test editable table model.
 */
public class TestTableModel extends AbstractTableModel {
    private Map cellValues = new HashMap();

    public int getRowCount() {
        return 2;
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Point coordinate = new Point(rowIndex, columnIndex);
        if (cellValues.containsKey(coordinate)) return cellValues.get(coordinate);
        return rowIndex + "," + columnIndex;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Point coordinate = new Point(rowIndex, columnIndex);
        cellValues.put(coordinate, aValue);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}

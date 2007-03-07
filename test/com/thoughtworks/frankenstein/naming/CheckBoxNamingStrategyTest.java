package com.thoughtworks.frankenstein.naming;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import junit.framework.TestCase;
import com.thoughtworks.frankenstein.common.WaitForIdle;

/**
 * Ensures behaviour of CheckBox naming
 */
public class CheckBoxNamingStrategyTest extends TestCase {

    public void testNamesCheckBoxesBasedOnName() {
        JCheckBox box = new JCheckBox("CheckBox text");
        new CheckBoxNamingStrategy("").name(box, 0);
        assertEquals("CheckBoxtext", box.getName());
    }

    public void testNamesEditorCheckBoxesAfterColumnInTable() {
        JTable table = new JTable(new TestTableModel());
        table.editCellAt(0,0);
        new WaitForIdle().waitForIdle();
        JCheckBox editorCheckBox = (JCheckBox) table.getEditorComponent();
        new CheckBoxNamingStrategy("").name(editorCheckBox, 0);
        assertEquals("CheckBoxColumn", editorCheckBox.getName());
    }

    private static class TestTableModel extends AbstractTableModel {
        private Boolean value = Boolean.FALSE;

        public int getRowCount() {
            return 1;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public int getColumnCount() {
            return 3;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getColumnName(int column) {
            if (column == 0) return "Check Box Column";
            return super.getColumnName(column);
        }

        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Boolean.class;
            return super.getColumnClass(columnIndex);
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0: return value;
                case 1: return "foo";
                case 2: return "bar";
            }
            throw new RuntimeException("Foo");
        }


        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            value = (Boolean) aValue;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return true;
            return false;
        }
    }
}

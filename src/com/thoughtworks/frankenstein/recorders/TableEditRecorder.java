package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import com.thoughtworks.frankenstein.events.CancelTableEditEvent;
import com.thoughtworks.frankenstein.events.EditTableCellEvent;
import com.thoughtworks.frankenstein.events.StopTableEditEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording table edit events
 * @author Vivek Prahlad
 */
public class TableEditRecorder extends AbstractComponentRecorder implements PropertyChangeListener, CellEditorListener {
    private static final String TABLE_CELL_EDITOR_PROPERTY = "tableCellEditor";
    private int editingRow;
    private int editingColumn;
    private JTable table;
    private TableCellEditor editor;

    public TableEditRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JTable.class);
    }

    void componentShown(Component component) {
        table(component).addPropertyChangeListener(TABLE_CELL_EDITOR_PROPERTY, this);
    }

    private JTable table (Component component) {
        return (JTable) component;
    }

    void componentHidden(Component component) {
        table(component).removePropertyChangeListener(TABLE_CELL_EDITOR_PROPERTY, this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        final JTable table = (JTable) evt.getSource();
        //The invokeLater is needed because we get notified about the editor change *before* the editing row and
        //coordinate are changed :-(
        if (hasEditor(evt)) {
            invokeLater(table);
        } else {
            removeCellEditorListener();
            recorder.record(new CancelTableEditEvent(tableName(table)));
        }
    }

    private boolean hasEditor(PropertyChangeEvent evt) {
        //Since the property change is called whenever the editor is set (it is set to null when editing stops),
        //ensure that the table has actually started an edit.
        return evt.getNewValue()!=null;
    }

    private void invokeLater(final JTable table) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                storeTable(table);
                addCellEditorListener();
                recorder.record(new EditTableCellEvent(tableName(table), editingRow, editingColumn));
            }
        });
    }

    private void addCellEditorListener() {
        editor.addCellEditorListener(this);
    }

    private void storeTable(JTable table) {
        this.table = table;
        editingRow = table.getEditingRow();
        editingColumn = table.getEditingColumn();
        editor = table.getCellEditor();
    }

    public void editingStopped(final ChangeEvent e) {
        removeCellEditorListener();
        recorder.record(new StopTableEditEvent(tableName(table)));
    }

    private void removeCellEditorListener() {
        if (editor!=null) {
            editor.removeCellEditorListener(TableEditRecorder.this);
        }
    }

    public void editingCanceled(ChangeEvent e) {
    }

    private String tableName(JTable table) {
        return componentName(table);
    }
}

package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.*;

import com.thoughtworks.frankenstein.events.SelectTableRowEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording table selection
 *
 * @author vivek
 */
public class SelectTableRecorder extends AbstractComponentRecorder implements ListSelectionListener, TableColumnModelListener, PropertyChangeListener {
    private Map listSelectionModelToTable = new HashMap();
    private Map columnSelectionModelToTableMap = new HashMap();
    private static final String SELECTION_MODEL_PROPERTY = "selectionModel";

    public SelectTableRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JTable.class);
    }

    void componentShown(Component component) {
        table(component).getColumnModel().addColumnModelListener(this);
        listSelectionModelToTable.put(table(component).getSelectionModel(), component);
        table(component).addPropertyChangeListener(SELECTION_MODEL_PROPERTY, this);
        table(component).getSelectionModel().addListSelectionListener(this);
    }

    private JTable table(Component component) {
        return (JTable) component;
    }

    void componentHidden(Component component) {
        table(component).getColumnModel().removeColumnModelListener(this);
        table(component).removePropertyChangeListener(SELECTION_MODEL_PROPERTY, this);
        table(component).getSelectionModel().removeListSelectionListener(this);
        listSelectionModelToTable.remove(listSelectionModelToTable.remove(table(component).getSelectionModel()));
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        JTable table = (JTable) listSelectionModelToTable.get(e.getSource());
        if (table.getRowSelectionAllowed() && table.getSelectedRow() != -1) {
            if (!table.getColumnSelectionAllowed()) {
                recorder.record(new SelectTableRowEvent(componentName(table), table.getSelectedRows()));
            }
        }
    }

    public void columnAdded(TableColumnModelEvent e) {
    }

    public void columnRemoved(TableColumnModelEvent e) {
    }

    public void columnMoved(TableColumnModelEvent e) {
    }

    public void columnMarginChanged(ChangeEvent e) {
    }

    public void columnSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
//        JTable table = (JTable) e.getSource();
//        table.getSelectedRows();
//        table.getCellSelectionEnabled();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        ListSelectionModel oldModel = (ListSelectionModel) evt.getOldValue();
        ListSelectionModel newModel = (ListSelectionModel) evt.getNewValue();
        JTable table = (JTable) listSelectionModelToTable.remove(oldModel);
        newModel.addListSelectionListener(this);
        oldModel.removeListSelectionListener(this);
        listSelectionModelToTable.put(newModel, table);
    }
}

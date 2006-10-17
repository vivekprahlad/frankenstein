package com.thoughtworks.frankenstein.ui;

import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.recorders.EventRecorder;

/**
 * Data model for recorded events.
 *
 * @author Vivek Prahlad
 */
public class RecorderTableModel extends AbstractTableModel implements ChangeListener {
    private List eventList;
    private static final int ACTION_COLUMN = 0;
    private static final int TARGET_COLUMN = 1;
    private static final int PARAMETER_COLUMN = 2;
    private EventRecorder recorder;

    public RecorderTableModel(EventRecorder recorder) {
        this.recorder = recorder;
        this.eventList = recorder.eventList();
        recorder.addChangeListener(this);
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case ACTION_COLUMN:
                return "Action";
            case TARGET_COLUMN:
                return "Target";
            case PARAMETER_COLUMN:
                return "Parameters";
        }
        throw new RuntimeException("Unknown column: " + columnIndex);
    }

    public int getRowCount() {
        return eventList.size();
    }

    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        FrankensteinEvent event = (FrankensteinEvent) eventList.get(rowIndex);
        switch (columnIndex) {
            case ACTION_COLUMN:
                return event.action();
            case TARGET_COLUMN:
                return event.target();
            case PARAMETER_COLUMN:
                return event.parameters();
        }
        throw new RuntimeException("Unknown column: " + columnIndex);
    }

    public void stateChanged(ChangeEvent evt) {
        eventList = recorder.eventList();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                fireTableDataChanged();
            }
        });
    }
}

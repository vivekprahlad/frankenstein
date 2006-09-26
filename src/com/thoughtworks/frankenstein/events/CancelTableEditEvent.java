package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands recording table edit events
 * @author Vivek Prahlad
 */
public class CancelTableEditEvent extends AbstractFrankensteinEvent {
    private String tableName;
    static final String ACTION = "CancelTableEdit";

    public CancelTableEditEvent(String tableName) {
        this.tableName = tableName;
    }

    public String toString() {
        return "CancelTableEditEvent: " + "Table: " + tableName;
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (isLastEventTableEdit(lastEvent)) {
            list.removeLastEvent();
        } else if (isLastEventTableEditStopped(lastEvent)) {
            //Do nothing
        } else {
            list.addEvent(this);
        }
    }

    private boolean isLastEventTableEditStopped(FrankensteinEvent lastEvent) {
        return lastEvent.action().equals(StopTableEditEvent.ACTION);
    }

    private boolean isLastEventTableEdit(FrankensteinEvent lastEvent) {
        return lastEvent.action().equals(EditTableCellEvent.EDIT_TABLE_CELL_ACTION) && lastEvent.target().equals(target());
    }

    public String target() {
        return tableName;
    }

    public String parameters() {
        return "";
    }

    public void run() {
        JTable table = (JTable) finder.findComponent(context, tableName);
        table.getCellEditor().cancelCellEditing();
    }
}

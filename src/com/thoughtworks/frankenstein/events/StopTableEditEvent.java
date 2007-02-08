package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Understands stopping a table edit.
 *
 * @author Vivek Prahlad
 */
public class StopTableEditEvent extends AbstractFrankensteinEvent {
    private String tableName;
    public static final String ACTION = "StopTableEdit";

    public StopTableEditEvent(String tableName) {
        this.tableName = tableName;
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (lastEvent.action().equals(CancelTableEditEvent.ACTION)) {
            list.replaceLastEvent(this);
        } else {
            super.record(list, lastEvent);
        }
    }

    public String target() {
        return tableName;
    }

    public String toString() {
        return "StopTableEditEvent: Table: " + tableName;
    }

    public void run() {
        finder.setTableCellEditor(null);
        JTable table = (JTable) finder.findComponent(context, tableName);
        table.repaint();
    }
}

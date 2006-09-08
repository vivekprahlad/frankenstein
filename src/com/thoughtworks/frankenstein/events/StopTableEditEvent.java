package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands stopping a table edit.
 * @author Vivek Prahlad
 */
public class StopTableEditEvent extends AbstractFrankensteinEvent {
    private String tableName;
    public static final String ACTION = "StopTableEdit";

    public StopTableEditEvent(String tableName) {
        this.tableName = tableName;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        finder.setTableCellEditor(null);
        JTable table = (JTable) finder.findComponent(context, tableName);
        table.repaint();
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
}

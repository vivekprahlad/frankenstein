package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.actions.Action;

/**
 * Understands actions on table rows
 */
public class TableRowEvent extends AbstractCompoundEvent {
    private String tableName;
    private int rowIndex;

    public TableRowEvent(String tableName, int rowIndex, Action action) {
        super(action);
        this.tableName = tableName;
        this.rowIndex = rowIndex;
    }

    public TableRowEvent(String args, Action action) {
        this(params(args)[0], Integer.parseInt(params(args)[1]), action);
    }

    public String toString() {
        return action.name() + "TableRowEvent: " + tableName + SPACE + Integer.toString(rowIndex);
    }

    public String target() {
        return tableName;
    }

    public String parameters() {
        return Integer.toString(rowIndex);
    }

    public synchronized void run() {
        JTable table = (JTable) finder.findComponent(context, tableName);
        action.execute(getLocation(table), table, finder, context);
    }

    private Point getLocation(JTable table) {
        Point point = table.getLocation();
        Rectangle cellRect = table.getCellRect(rowIndex, 0, true);
        point.translate(cellRect.x, cellRect.y + table.getRowHeight(rowIndex) / 2);
        return point;
    }

    public String scriptLine(ScriptStrategy scriptStrategy) {
        return scriptStrategy.toMethod(action()) + scriptStrategy.enclose(quote(target()) + " , " + scriptStrategy.escape(rowIndex));
    }
}


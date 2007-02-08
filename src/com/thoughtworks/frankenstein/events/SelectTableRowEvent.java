package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Selects specified rows in a table
 *
 * @author vivek
 */
public class SelectTableRowEvent extends AbstractFrankensteinEvent {
    private String tableName;
    private int[] rows;

    public SelectTableRowEvent(String table, int[] rows) {
        this.tableName = table;
        this.rows = rows;
    }

    public SelectTableRowEvent(String scriptLine) {
        this(params(scriptLine)[0], intArray(params(scriptLine)[1]));
    }

    private static int[] intArray(String scriptLine) {
        String[] rowString = scriptLine.split(",");
        int[] values = new int[rowString.length];
        for (int i = 0; i < rowString.length; i++) {
            values[i] = Integer.parseInt(rowString[i]);
        }
        return values;
    }

    public String toString() {
        return "SelectTableRowEvent: " + tableName + " {" + parameters() + "}";
    }

    public String target() {
        return tableName;
    }

    public String parameters() {
        String values = "";
        for (int i = 0; i < rows.length; i++) {
            values += String.valueOf(rows[i]) + ",";
        }
        return values.substring(0, values.length() - 1);
    }

    public void run() {
        JTable table = (JTable) finder.findComponent(context, tableName);
        table.getSelectionModel().setSelectionInterval(rows[0], rows[0]);
        for (int i = 1; i < rows.length; i++) {
            int row = rows[i];
            table.getSelectionModel().addSelectionInterval(row, row);
        }
    }
}

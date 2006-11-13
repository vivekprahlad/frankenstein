package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Understands checking number of rows in a table
 */
public class AssertTableRowsEvent extends AbstractFrankensteinEvent {
    private String table;
    private int numberOfRows;

    public AssertTableRowsEvent(String table, int numberOfRows) {
        this.table = table;
        this.numberOfRows = numberOfRows;
    }

    public String toString() {
        return "AssertTableRowsEvent: " + table + ", " + numberOfRows;
    }

    public AssertTableRowsEvent(String args) {
        this(params(args)[0],Integer.parseInt(params(args)[1]));
    }

    public String target() {
        return table;
    }

    public String parameters(){
           return Integer.toString(numberOfRows);
    }
    
    public void run() {
        JTable tableComponent = (JTable) finder.findComponent(context, table);
        if (numberOfRows != (tableComponent.getRowCount()))
            throw new RuntimeException("Expected \"" + numberOfRows + "\" but was \"" + tableComponent.getRowCount() + "\"");
    }
}

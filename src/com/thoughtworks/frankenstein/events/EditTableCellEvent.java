package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Understands recording table edit events
 *
 * @author Vivek Prahlad
 */
public class EditTableCellEvent extends AbstractFrankensteinEvent {
    private String tableName;
    int row, column;
    static final String EDIT_TABLE_CELL_ACTION = "EditTableCell";

    public EditTableCellEvent(String tableName, int row, int column) {
        this.tableName = tableName;
        this.row = row;
        this.column = column;
    }

    public EditTableCellEvent(String scriptLine) {
        this(params(scriptLine)[0], row(params(scriptLine)[1]), column(params(scriptLine)[1]));
    }

    private static String[] coordinates(String params) {
        return params.split(",");
    }

    private static int row(String params) {
        return Integer.parseInt(coordinates(params)[0]);
    }

    private static int column(String params) {
        return Integer.parseInt(coordinates(params)[1]);
    }

    public String toString() {
        return "EditTableCellEvent: " + "Table: " + tableName + " cell: (" + row + "," + column + ")";
    }

    public String target() {
        return tableName;
    }

    public String parameters() {
        return row + "," + column;
    }

    public void run() {
        final JTable table = (JTable) finder.findComponent(context, tableName);
        table.editCellAt(row, column);
        finder.setTableCellEditor(table.getEditorComponent());
    }

    public String scriptLine(ScriptStrategy scriptStrategy) {
        return scriptStrategy.toMethod(action()) + scriptStrategy.enclose(quote(target()) + " , " + scriptStrategy.cell(row, column));
    }
}

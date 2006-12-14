package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.events.actions.Action;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Actions on table headers.
 */
public class TableHeaderEvent extends AbstractCompoundEvent {
    private String headerName;
    private String columnName;

    public TableHeaderEvent(String headerName, String columnName, Action action) {
        super(action);
        this.headerName = headerName;
        this.columnName = columnName;
    }

    public TableHeaderEvent(String args, Action action) {
        this(params(args)[0], params(args)[1], action);
    }

    public String target() {
        return headerName;
    }

    public String parameters() {
        return columnName;
    }

    public String toString() {
        return "TableHeaderEvent: " + headerName + " " + columnName;
    }

    public synchronized void run() {
        JTableHeader header = (JTableHeader) finder.findComponent(context, headerName);
        action.execute(getLocation(header), header, finder, context);
    }

    private Point getLocation(JTableHeader header) {
        TableColumnModel columnModel = header.getColumnModel();
        Point point = header.getLocation();
        int i;
        for (i = 0; i < getColumnIndex(columnModel); i++) {
            point.x += columnModel.getColumn(i).getWidth();
        }
        point.x += (columnModel.getColumn(i).getWidth()) / 2;
        point.y += (header.getHeight()) / 2;
        return point;
    }

    private int getColumnIndex(TableColumnModel columnModel) {
        for (int i = 0; i < columnModel.getColumnCount(); i++)
            if (columnModel.getColumn(i).getHeaderValue().equals(columnName))
                return i;
        return -1;
    }
}

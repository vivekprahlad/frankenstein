package com.thoughtworks.frankenstein.events;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cprakash
 * Date: Nov 20, 2006
 * Time: 5:17:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClickTableHeaderEvent extends AbstractFrankensteinEvent {

    private String headerName;
    private String columnName;

    public String toString() {
        return "ClickTableHeaderEvent: " + headerName + " " + columnName;
    }

    public ClickTableHeaderEvent(String headerName, String columnName) {
        this.headerName = headerName;
        this.columnName = columnName;
    }

    public ClickTableHeaderEvent(String args) {
        this(params(args)[0], params(args)[1]);
    }

   public String target() {
        return headerName;
    }

    public String parameters() {
        return columnName;
    }

    public synchronized void run() {
        JTableHeader header = (JTableHeader) finder.findComponent(context, headerName);
        singleClick(header, getLocation(header));
    }

    private Point getLocation(JTableHeader header) {
        TableColumnModel columnModel = header.getColumnModel();
        Point point = header.getLocation();
        int i;
        for (i = 0; i < (getColumnIndex(columnModel) - 1); i++)
            point.x += columnModel.getColumn(i).getWidth();
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

    private void singleClick(JTableHeader header, Point point) {
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(header, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, point.x, point.y, 1, false,MouseEvent.BUTTON1));
    }
}

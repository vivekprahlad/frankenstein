package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.HierarchyEvent;

/**

 */
public class RightClickTableRowsEvent extends AbstractFrankensteinEvent implements AWTEventListener {
    private String tableName;
    private int rowIndex;

    public RightClickTableRowsEvent(String tableName, int rowIndex) {
        this.tableName = tableName;
        this.rowIndex = rowIndex;
        this.eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    public RightClickTableRowsEvent(String args) {
        this(params(args)[0], Integer.parseInt(params(args)[1]));
    }

    public String toString() {
        return "RightClickTableRowsEvent: " + tableName + " " + Integer.toString(rowIndex);
    }

    public String target() {
        return tableName;
    }

    public String parameters() {
        return Integer.toString(rowIndex);
    }

    public synchronized void run() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.HIERARCHY_EVENT_MASK);
        JTable table = (JTable) finder.findComponent(context, tableName);
        rightClick(table, getLocation(table));
        try {
            wait(10000);
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Point getLocation(JTable table) {
        Point point = table.getLocation();
        int i;
        for (i = 0; i < rowIndex - 1; i++)
            point.y += table.getRowHeight(i);
        point.y += (table.getRowHeight(i)) / 2;
        point.x += (table.getWidth()) / 2;
        return point;
    }

    private void rightClick(JTable table, Point point) {
//        Toolkit.getDefaultToolkit().getSystemEventQueue()
//                .postEvent(new MouseEvent(table, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, point.x, point.y, 0, true,MouseEvent.BUTTON3));
//        Toolkit.getDefaultToolkit().getSystemEventQueue()
//                .postEvent(new MouseEvent(table, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, point.x, point.y, 0, false,MouseEvent.BUTTON3));
//
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(table, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, point.x, point.y, 0, true, MouseEvent.BUTTON3));
    }

    public synchronized void eventDispatched(AWTEvent event) {
        if (event instanceof HierarchyEvent) {
            HierarchyEvent he = (HierarchyEvent) event;
            if (isComponentDisplayableEvent(he) && matchesComponentType(event)) {
                Component component = (Component) event.getSource();
                if (component.isDisplayable()) {
                    finder.menuDisplayed((JPopupMenu) component);
                }
                notifyAll();
            }
        }
    }

    private boolean matchesComponentType(AWTEvent event) {
        return event.getSource() instanceof JPopupMenu;
    }

    private boolean isComponentDisplayableEvent(HierarchyEvent he) {
        return (he.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0;
    }


}


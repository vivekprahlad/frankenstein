package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.recorders.EventList;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

public class DoubleClickListEvent extends AbstractFrankensteinEvent {
    private String listName;
    private int itemIndex;

    public DoubleClickListEvent(String listName, int itemIndex) {
        this.listName = listName;
        this.itemIndex = itemIndex;
    }

    public DoubleClickListEvent(String args) {
        this(params(args)[0], Integer.parseInt(params(args)[1]));
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        list.addEvent(this);
    }

    public String toString() {
        return "DoubleClickListEvent: " + listName + " " + itemIndex;
    }

    public String target() {
        return listName;
    }

    public String parameters() {
        return Integer.toString(itemIndex);
    }

    public synchronized void run() {
        JList list = (JList) finder.findComponent(context, listName);
        doubleClick(list, list.indexToLocation(itemIndex));
    }

    private void doubleClick(JList list, Point point) {
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, point.x, point.y, 2, false));
    }

}

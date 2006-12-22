package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.events.actions.*;
import com.thoughtworks.frankenstein.events.actions.Action;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

public class ListEvent extends AbstractCompoundEvent {
    private String listName;
    private int itemIndex;

    public ListEvent(String listName, int itemIndex, Action action) {
        super(action);
        this.listName =listName;
        this.itemIndex=itemIndex;
    }

    public ListEvent(String args, Action action) {
        this(params(args)[0], Integer.parseInt(params(args)[1]), action);
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        list.addEvent(this);
    }

    public String toString() {
        return "ListEvent: " + listName + " " + itemIndex;
    }

    public String target() {
        return listName;
    }

    public String parameters() {
        return Integer.toString(itemIndex);
    }

    public synchronized void run() {
        JList list = (JList) finder.findComponent(context, listName);
        action.execute(list.indexToLocation(itemIndex), list, finder, context);
    }
}

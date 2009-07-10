package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import com.thoughtworks.frankenstein.events.actions.Action;
import com.thoughtworks.frankenstein.recorders.EventList;

public class ListEvent extends AbstractCompoundEvent {
    private String listName;
    private int itemIndex;

    public ListEvent(String listName, int itemIndex, Action action) {
        super(action);
        this.listName = listName;
        this.itemIndex = itemIndex;
    }

    public ListEvent(String args, Action action) {
        this(params(args)[0], Integer.parseInt(params(args)[1]), action);
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        list.addEvent(this);
    }

    public String toString() {
        return "ListEvent: " + listName + SPACE + itemIndex;
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

    public String scriptLine(ScriptStrategy scriptStrategy) {
        return scriptStrategy.toMethod(action()) + scriptStrategy.enclose(quote(target()) + " , " + scriptStrategy.escape(itemIndex));
    }
}

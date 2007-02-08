package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Represents an internal frame being shown.
 *
 * @author Vivek Prahlad
 */
public class DialogClosedEvent extends AbstractFrankensteinEvent {
    private String title;
    public static final String DIALOG_CLOSED_ACTION = "DialogClosed";
    static final int DEFAULT_TIMEOUT = 10;

    public DialogClosedEvent(String title) {
        this.title = title;
        executeInPlayerThread();
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (lastEvent instanceof ActivateWindowEvent) {
            list.replaceLastEvent(this);
        } else {
            list.addEvent(this);
        }
    }

    public String toString() {
        return "DialogClosedEvent: " + title;
    }

    public String target() {
        return title;
    }

    public void run() {
        context.waitForDialogClosing(title, DEFAULT_TIMEOUT);
    }
}

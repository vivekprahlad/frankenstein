package com.thoughtworks.frankenstein.events;

/**
 * Represents an internal frame being shown.
 * @author Vivek Prahlad
 */
public class DialogClosedEvent extends AbstractFrankensteinEvent {
    private String title;
    public static final String DIALOG_SHOWN_ACTION = "DialogShown";
    static final int DEFAULT_TIMEOUT = 10;

    public DialogClosedEvent(String title) {
        this.title = title;
        eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    public String toString() {
        return "DialogClosedEvent: " + title;
    }

    public String target() {
        return title;
    }

    public void run() {
        context.waitForDialogClosing(title, DialogClosedEvent.DEFAULT_TIMEOUT);
    }
}

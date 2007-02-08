package com.thoughtworks.frankenstein.events;

/**
 * Represents a Dialog being shown.
 *
 * @author Vivek Prahlad
 */
public class DialogShownEvent extends AbstractFrankensteinEvent {
    private String title;
    public static final String DIALOG_SHOWN_ACTION = "DialogShown";
    static final int DEFAULT_TIMEOUT = 10;

    public DialogShownEvent(String title) {
        this.title = title;
        executeInPlayerThread();
    }

    public String toString() {
        return "DialogShownEvent: " + title;
    }

    public String target() {
        return title;
    }

    public void run() {
        try {
            context.waitForDialogOpening(title, DEFAULT_TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

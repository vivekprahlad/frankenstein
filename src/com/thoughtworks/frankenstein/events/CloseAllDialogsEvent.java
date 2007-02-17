package com.thoughtworks.frankenstein.events;

/**
 * Closes all open dialogs recursively.
 *
 * @author Vivek Prahlad
 */
public class CloseAllDialogsEvent extends AbstractFrankensteinEvent {

    public CloseAllDialogsEvent(String scriptLine) {
        executeInPlayerThread();
    }

    public String toString() {
        return "CloseAllDialogsEvent";
    }

    public String scriptLine() {
        return underscore(action());
    }

    public String target() {
        return "";
    }

    public void run() {
        context.closeAllDialogs();
    }
}

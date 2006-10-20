package com.thoughtworks.frankenstein.events;

/**
 * Represents a Dialog being shown.
 * @author Vivek Prahlad
 */
public class CloseAllDialogsEvent extends AbstractFrankensteinEvent {

    public CloseAllDialogsEvent(String scriptLine) {
        eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
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

package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.recorders.EventList;

import javax.swing.*;

/**
 * Understands recording button clicks.
 *
 * @author Vivek Prahlad
 */
public class ClickButtonEvent extends AbstractFrankensteinEvent {
    private String buttonName;
    public static final String CLICK_BUTTON_ACTION = "ClickButton";
    private static final Object LOCK = new Object();
    private Exception exception;

    public ClickButtonEvent(String buttonName) {
        this.buttonName = buttonName;
        this.eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        list.addEvent(this);
    }

    public String toString() {
        return "ClickButtonEvent: " + buttonName;
    }

    public String target() {
        return buttonName;
    }

    public void run() {
        AbstractButton button = (AbstractButton) finder.findComponent(context, buttonName);
        new ClickButtonAction().execute(button, context);
    }
}

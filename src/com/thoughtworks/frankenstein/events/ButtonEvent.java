package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import com.thoughtworks.frankenstein.events.actions.Action;
import com.thoughtworks.frankenstein.recorders.EventList;


/**
 * Understands button actions.
 *
 * @author Vivek Prahlad
 * ahlad
 */
public class ButtonEvent extends AbstractCompoundEvent {
    private String buttonName;
    public static final String CLICK_BUTTON_ACTION = "ClickButton";

    public ButtonEvent(String scriptLine, Action action) {
        super(action);
        this.buttonName = scriptLine;
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        list.addEvent(this);
    }

    public String toString() {
        return "ButtonEvent: " + buttonName;
    }

    public String target() {
        return buttonName;
    }

    public void run() {
        AbstractButton button = (AbstractButton) finder.findComponent(context, buttonName);
        action.execute(center(button), button, finder, context);
    }

}

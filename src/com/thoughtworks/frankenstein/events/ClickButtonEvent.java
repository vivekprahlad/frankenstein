package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.WindowContextListener;

import javax.swing.*;
import java.awt.event.ActionListener;

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

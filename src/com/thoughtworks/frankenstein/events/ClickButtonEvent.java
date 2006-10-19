package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Understands recording button clicks.
 * @author Vivek Prahlad
 */
public class ClickButtonEvent extends AbstractFrankensteinEvent {
    private String buttonName;
    public static final String CLICK_BUTTON_ACTION = "ClickButton";

    public ClickButtonEvent(String buttonName) {
        this.buttonName = buttonName;
    }

    public String toString() {
        return "ClickButtonEvent: " + buttonName;
    }

    public String target() {
        return buttonName;
    }

    public synchronized void run() {
        final AbstractButton button =  (AbstractButton) finder.findComponent(context, buttonName);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                button.doClick();
            }
        });
    }
}

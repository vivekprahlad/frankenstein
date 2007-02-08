package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Represents a menu navigation event.
 *
 * @author Vivek Prahlad
 */
public class NavigateEvent extends AbstractFrankensteinEvent {
    protected String path;

    public NavigateEvent(String path) {
        this.path = path;
        executeInPlayerThread();
    }

    public String toString() {
        return "NavigateEvent: " + path;
    }

    public String target() {
        return path;
    }

    public void run() {
        final JMenuItem menuItem = finder.findMenuItem(context, path);
        if (menuItem.isEnabled()) {
            new ClickButtonAction().execute(menuItem, context);
            MenuSelectionManager.defaultManager().clearSelectedPath();
        } else {
            throw new RuntimeException("Menu item: " + path + " is disabled");
        }
    }
}

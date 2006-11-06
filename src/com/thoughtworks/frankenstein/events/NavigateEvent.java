package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Represents a menu navigation event.
 * @author Vivek Prahlad
 */
public class NavigateEvent extends AbstractFrankensteinEvent {
    protected String path;

    public NavigateEvent(String path) {
        this.path = path;
        this.eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
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
        } else {
            throw new RuntimeException("Menu item: "+ path + " is disabled");
        }
    }
}

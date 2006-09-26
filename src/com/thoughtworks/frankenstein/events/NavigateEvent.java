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
    }

    public String toString() {
        return "NavigateEvent: " + path;
    }

    public String target() {
        return path;
    }

    public void run() {
        JMenuItem menuItem = finder.findMenuItem(context, path);
        menuItem.doClick();
    }
}

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
    private String path;

    public NavigateEvent(String path) {
        this.path = path;
    }

    public String toString() {
        return "NavigateEvent: " + path;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        JMenuItem menuItem = finder.findMenuItem(context, path);
        menuItem.doClick();
    }

    public String target() {
        return path;
    }
}

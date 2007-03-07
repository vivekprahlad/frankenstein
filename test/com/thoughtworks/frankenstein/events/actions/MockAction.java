package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Stores action execution location.
 */
public class MockAction implements Action {
    public Point point;

    public void execute(Point location, JComponent component, ComponentFinder finder, WindowContext windowContext) {
        point = location;
    }

    public String name() {
        return null;
    }

    public boolean wasNotCalled() {
        return point == null;
    }
}
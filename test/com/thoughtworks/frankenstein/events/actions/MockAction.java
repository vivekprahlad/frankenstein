package com.thoughtworks.frankenstein.events.actions;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

import javax.swing.*;
import java.awt.*;

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
}
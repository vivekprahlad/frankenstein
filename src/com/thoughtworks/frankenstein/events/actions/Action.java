package com.thoughtworks.frankenstein.events.actions;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

import javax.swing.*;
import java.awt.*;

/**
 * Interface for Actions.
 * @author Vivek Prahlad
 */
public interface Action {
    void execute(Point location, JComponent component, ComponentFinder finder, WindowContext windowContext);
    String name();
}

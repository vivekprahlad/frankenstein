package com.thoughtworks.frankenstein.events.actions;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;
import java.awt.*;

/**
 * Understands SOMETHING
 *
 * @author vivek
 */
public interface Action {
    void execute(Point location, JComponent component, ComponentFinder finder);
    String name();
}

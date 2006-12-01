package com.thoughtworks.frankenstein.events.actions;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;
import java.awt.*;

/**
 * Understands clicking on a component
 *
 * @author vivek
 */
public class ClickAction implements Action{
    public void execute(Point location, JComponent component, ComponentFinder finder) {
    }

    public String name() {
        return null;
    }
}

package com.thoughtworks.frankenstein.events.actions;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.WindowContextListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

/**
 * Understands clicking on a component
 *
 * @author vivek
 */
public class ClickAction extends MouseAction implements Action {

    protected void postEvents(JComponent component, Point location, EventQueue queue) {
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_PRESSED, 0));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_RELEASED, 0));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_CLICKED, 1));
    }

    public String name() {
        return "Click";
    }

    public boolean equals(Object obj) {
        return obj instanceof ClickAction;
    }

    public int hashCode() {
        return 1;
    }
}

package com.thoughtworks.frankenstein.events.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Understands double clicking on a component
 *
 * @author vivek
 */
public class DoubleClickAction extends MouseAction implements Action {

    protected void postEvents(JComponent component, Point location, EventQueue queue) {
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_CLICKED, 2));

    }

    public String name() {
        return "DoubleClick";
    }


    public boolean equals(Object obj) {
        return obj instanceof MouseAction;
    }


    public int hashCode() {
        return 1;
    }
}

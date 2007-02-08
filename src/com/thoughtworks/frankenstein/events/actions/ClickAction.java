package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import javax.swing.*;

/**
 * Understands clicking on a component
 *
 * @author vivek
 */
public class ClickAction extends AbstractMouseAction implements Action {

    protected void postEvents(JComponent component, Point location, EventQueue queue) {
        click(queue, component, location, 1);
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

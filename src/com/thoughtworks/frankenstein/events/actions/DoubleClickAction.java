package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Understands double clicking on a component
 *
 * @author vivek
 */
public class DoubleClickAction extends AbstractMouseAction implements Action {

    protected void postEvents(JComponent component, Point location, EventQueue queue) {
        click(queue, component, location, 1);
        click(queue, component, location, 2);
    }

    public String name() {
        return "DoubleClick";
    }

    public synchronized void mouseReleased(MouseEvent e) {
        if (e.getClickCount() == 2) {
            notifyAll(); //Fix for bug FKS-4
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof AbstractMouseAction;
    }

    public int hashCode() {
        return 1;
    }
}

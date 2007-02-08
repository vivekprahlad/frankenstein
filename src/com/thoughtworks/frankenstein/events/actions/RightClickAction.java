package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Understands Right Clicking on a specified location.
 *
 * @author Vivek Prahlad
 */
public class RightClickAction implements AWTEventListener, Action {
    private ComponentFinder finder;

    public synchronized void execute(Point location, JComponent component, ComponentFinder finder, WindowContext windowContext) {
        this.finder = finder;
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.HIERARCHY_EVENT_MASK);
        rightClick(location, component);
        try {
            wait(1000);
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String name() {
        return "RightClick";
    }

    public synchronized void eventDispatched(AWTEvent event) {
        if (event instanceof HierarchyEvent) {
            HierarchyEvent he = (HierarchyEvent) event;
            if (isComponentDisplayableEvent(he) && matchesComponentType(event)) {
                Component component = (Component) event.getSource();
                if (component.isDisplayable()) {
                    finder.menuDisplayed((JPopupMenu) component);
                }
                notifyAll();
            }
        }
    }

    private boolean matchesComponentType(AWTEvent event) {
        return event.getSource() instanceof JPopupMenu;
    }

    private boolean isComponentDisplayableEvent(HierarchyEvent he) {
        return (he.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0;
    }

    private void rightClick(Point point, JComponent source) {
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(source, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, point.x, point.y, 0, true, MouseEvent.BUTTON3));
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(source, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, point.x, point.y, 0, true, MouseEvent.BUTTON3));
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(source, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, point.x, point.y, 1, true, MouseEvent.BUTTON3));
    }

    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass();
    }

    public int hashCode() {
        return 1;
    }
}

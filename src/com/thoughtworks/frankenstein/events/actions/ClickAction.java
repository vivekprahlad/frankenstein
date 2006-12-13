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
public class ClickAction extends MouseAdapter implements Action, WindowContextListener {
    public synchronized void execute(Point location, JComponent component, ComponentFinder finder, WindowContext windowContext) {
        addListeners(windowContext, component);
        postEvents(component, location, Toolkit.getDefaultToolkit().getSystemEventQueue());
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        removeListeners(component, windowContext);
    }

    private void addListeners(WindowContext windowContext, JComponent component) {
        windowContext.addWindowContextListener(this);
        component.addMouseListener(this);
    }

    private void removeListeners(JComponent component, WindowContext windowContext) {
        component.removeMouseListener(this);
        windowContext.removeWindowContextListener(this);
    }

    private void postEvents(JComponent component, Point location, EventQueue queue) {
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_PRESSED, 0));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_RELEASED, 0));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_CLICKED, 1));
    }

    private MouseEvent creatMouseEvent(JComponent component, Point location, int type, int clickCount) {
        return new MouseEvent(component, type, System.currentTimeMillis(), 0, location.x, location.y, clickCount, false, MouseEvent.BUTTON1);
    }

    public String name() {
        return "Click";
    }

    public synchronized void mouseClicked(MouseEvent e) {
        notifyAll();
    }

    public synchronized void dialogShown() {
        notifyAll();
    }


    public boolean equals(Object obj) {
        return  obj instanceof ClickAction;
    }


    public int hashCode() {
        return 1;
    }
}

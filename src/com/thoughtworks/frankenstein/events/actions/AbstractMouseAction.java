package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.WindowContextListener;

/**
 * Base class for all mouse actions
 */
public abstract class AbstractMouseAction extends MouseAdapter implements WindowContextListener {
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

    protected abstract void postEvents(JComponent component, Point location, EventQueue queue);

    protected MouseEvent creatMouseEvent(JComponent component, Point location, int type, int clickCount) {
        return new MouseEvent(component, type, System.currentTimeMillis(), 0, location.x, location.y, clickCount, false, MouseEvent.BUTTON1);
    }

    public synchronized void mouseReleased(MouseEvent e) {
        notifyAll();
    }

    public synchronized void dialogShown() {
        notifyAll();
    }

    protected void click(EventQueue queue, JComponent component, Point location, int clickCount) {
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_PRESSED, clickCount));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_RELEASED, clickCount));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_CLICKED, clickCount));
    }
}

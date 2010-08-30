package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.WindowContextListener;

/**
 * Understands the Dropping of a component.
 *
 * @author Pavan
 */
public class DropAction extends MouseInputAdapter implements Action, WindowContextListener {
    public synchronized void execute(Point location, JComponent component, ComponentFinder finder, WindowContext windowContext){
        addListeners(windowContext, component);
        postEvents(component, location, Toolkit.getDefaultToolkit().getSystemEventQueue());
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            removeListeners(component, windowContext);
        }
    }

    private void addListeners(WindowContext windowContext, JComponent component) {
        windowContext.addWindowContextListener(this);
        component.addMouseMotionListener(this);
        component.addMouseListener(this);
    }

    private void removeListeners(JComponent component, WindowContext windowContext) {
        component.removeMouseListener(this);
        component.removeMouseMotionListener(this);
        windowContext.removeWindowContextListener(this);
    }

     protected MouseEvent creatMouseEvent(JComponent component, Point location, int type, int clickCount) {
        return new MouseEvent(component, type, System.currentTimeMillis(), 0, location.x, location.y, clickCount, false, MouseEvent.BUTTON1);
    }

    protected void postEvents(JComponent component, Point location, EventQueue queue) {
        queue.postEvent(creatMouseEvent(component, location, java.awt.event.MouseEvent.MOUSE_DRAGGED, 1));
        queue.postEvent(creatMouseEvent(component, location, java.awt.event.MouseEvent.MOUSE_RELEASED, 1));
    }

    public String name() {
        return "Drop";
    }

    public synchronized void mouseReleased(MouseEvent e) {
        notifyAll();
    }

    public synchronized void dialogShown() {
        notifyAll();
    }
}

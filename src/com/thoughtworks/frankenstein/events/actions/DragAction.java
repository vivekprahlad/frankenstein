package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.WindowContextListener;

/**
 * Understands the Dragging of a component.
 * 
 * @author Pavan
 */
public class DragAction extends MouseInputAdapter implements Action, WindowContextListener {

    public synchronized void execute(Point location, JComponent component, ComponentFinder finder, WindowContext windowContext) {
        addListeners(windowContext, component);
        drag(Toolkit.getDefaultToolkit().getSystemEventQueue(), component, location, 1);
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            removeListeners(component, windowContext);
        }
    }

    protected void drag(EventQueue queue, JComponent component, Point location, int clickCount) {
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_PRESSED, clickCount));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_DRAGGED, clickCount));
        queue.postEvent(creatMouseEvent(component, location, MouseEvent.MOUSE_EXITED, clickCount));
    }

    protected MouseEvent creatMouseEvent(JComponent component, Point location, int type, int clickCount) {
        return new MouseEvent(component, type, System.currentTimeMillis(), 0, location.x, location.y, clickCount, false, MouseEvent.BUTTON1);
    }

    private void addListeners(WindowContext windowContext, JComponent component) {
        windowContext.addWindowContextListener(this);
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    private void removeListeners(JComponent component, WindowContext windowContext) {
        component.removeMouseMotionListener(this);
        component.removeMouseListener(this);
        windowContext.removeWindowContextListener(this);
    }

    public synchronized void mouseDragged(MouseEvent e) {
        notifyAll();
    }

    public String name() {
        return "Drag";
    }

    public synchronized void dialogShown() {
        notifyAll();
    }
}

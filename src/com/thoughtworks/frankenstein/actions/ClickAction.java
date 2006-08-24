package com.thoughtworks.frankenstein.actions;

import java.awt.*;
import java.awt.event.*;

/**
 * Understands clicking on a component.
 */
public class ClickAction extends AbstractMouseAction implements Action, MouseListener, AWTEventListener {

    public synchronized void execute(Component component, Robot robot) {
        moveMouse(component, robot);
        component.addMouseListener(this);
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_EVENT_MASK);
        click(robot);
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        component.removeMouseListener(this);
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public synchronized void mousePressed(MouseEvent e) {
    }

    public synchronized void mouseReleased(MouseEvent e) {
        notifyAll();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public synchronized void eventDispatched(AWTEvent event) {
        if (event.getID() == WindowEvent.WINDOW_OPENED) {
            notifyAll();
        }
    }
}

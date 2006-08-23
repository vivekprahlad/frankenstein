package com.thoughtworks.frankenstein.actions;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * Understands clicking on a component.
 */
public class ClickAction extends AbstractMouseAction implements Action, MouseListener {

    public synchronized void execute(Component component, Robot robot) {
        moveMouse(component, robot);
        component.addMouseListener(this);
        click(robot);
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        component.removeMouseListener(this);
    }

    public synchronized void mouseClicked(MouseEvent e) {
    }

    public synchronized void mousePressed(MouseEvent e) {
        notifyAll();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}

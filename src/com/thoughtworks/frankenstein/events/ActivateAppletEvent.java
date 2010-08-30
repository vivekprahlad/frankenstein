package com.thoughtworks.frankenstein.events;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.*;
import javax.swing.*;

/**
 * Understands activating an applet.
 *
 * @author Pavan
 */
public class ActivateAppletEvent extends AbstractFrankensteinEvent implements FocusListener {
    private String title;

    public String toString() {
        return "ActivateAppletEvent: "+title;
    }

    public ActivateAppletEvent(String title) {
        executeInPlayerThread();
        this.title = title;
    }

    public String target() {
        return title;
    }

    private void waitForFocus(Component component) {
        if (component.hasFocus()) return;
        component.addFocusListener(this);
        component.requestFocus();
        try {
            wait(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        component.removeFocusListener(this);
    }

    public synchronized void focusGained(FocusEvent e) {
        notifyAll();
    }

    public void focusLost(FocusEvent e) {
    }

    public synchronized void run() {
        JApplet applet = finder.findApplet(title);
        applet.setVisible(false);
        applet.setVisible(true);
        if (applet.getGlassPane().getClass() == JPanel.class) {
            waitForFocus(applet);
        } else {
            waitForFocus(applet.getGlassPane());
        }
    }
}

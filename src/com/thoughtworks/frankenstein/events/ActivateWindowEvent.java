package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Represents a top level frame being activated.
 *
 * @author Vivek Prahlad
 */
public class ActivateWindowEvent extends AbstractFrankensteinEvent implements FocusListener {
    private String title;

    public ActivateWindowEvent(String title) {
        this.title = title;
        executeInPlayerThread();
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (!(lastEvent.action().equals(DialogClosedEvent.DIALOG_CLOSED_ACTION) ||
                lastEvent.action().equals(DialogShownEvent.DIALOG_SHOWN_ACTION))) {
            super.record(list, lastEvent);
        }
    }

    public String toString() {
        return "ActivateWindowEvent: " + title;
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

    public String target() {
        return title;
    }

    public synchronized void focusGained(FocusEvent e) {
        notifyAll();
    }

    public void focusLost(FocusEvent e) {
    }

    public synchronized void run() {
        Window window = finder.findWindow(title);
        Frame frame = (Frame) window;
        frame.setState(Frame.NORMAL);
        frame.setVisible(false);
        frame.setVisible(true);
        frame.toFront();
        RootPaneContainer rootPaneContainer = (RootPaneContainer) window;
        if (rootPaneContainer.getGlassPane().getClass() == JPanel.class) {
            waitForFocus(window);
        } else {
            waitForFocus(rootPaneContainer.getGlassPane());
        }
    }

    public String scriptLine(ScriptStrategy scriptStrategy) {
        return scriptStrategy.toMethod(action()) + scriptStrategy.enclose(quote(title));
    }
}

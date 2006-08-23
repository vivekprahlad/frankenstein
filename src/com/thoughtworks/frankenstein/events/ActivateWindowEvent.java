package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Represents an internal frame being shown.
 * @author Vivek Prahlad
 */
public class ActivateWindowEvent extends AbstractFrankensteinEvent implements FocusListener {
    private String title;

    public ActivateWindowEvent(String title) {
        this.title = title;
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (!lastEvent.action().equals(DialogShownEvent.DIALOG_SHOWN_ACTION))
            super.record(list, lastEvent);
    }

    public String toString() {
        return "ActivateWindowEvent: " + title;
    }

    public synchronized void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        Window window = finder.findWindow(title);
        Frame frame = (Frame) window;
        frame.setState(Frame.NORMAL);
        frame.setVisible(false);
        frame.setVisible(true);
        RootPaneContainer rootPaneContainer = (RootPaneContainer) window;
        if (rootPaneContainer.getGlassPane().getClass() == JPanel.class) {
            waitForFocus(window);
        } else {
            waitForFocus(rootPaneContainer.getGlassPane());
        }
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
}

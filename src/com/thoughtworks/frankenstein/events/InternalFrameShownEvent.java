package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Represents an internal frame being shown.
 * @author Vivek Prahlad
 */
public class InternalFrameShownEvent extends AbstractFrankensteinEvent {
    private String title;

    public InternalFrameShownEvent(String title) {
        this.title = title;
    }

    public String toString() {
        return "InternalFrameShownEvent: " + title;
    }

    public String target() {
        return title;
    }

    public void run() {
        JInternalFrame component = finder.findInternalFrame(context, title);
        if (component == null)
            throw new RuntimeException("Could not find JInternalFrame with title: " + title);
    }
}

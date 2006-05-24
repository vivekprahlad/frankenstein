package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

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

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        Component component = context.activeWindow();
        if (component instanceof JInternalFrame) {
            JInternalFrame frame = (JInternalFrame) component;
            if (title.equals(frame.getTitle()))
                return;
        }
        throw new RuntimeException("Could not find JInternalFrame with title: " + title);
    }

    public String target() {
        return title;
    }
}

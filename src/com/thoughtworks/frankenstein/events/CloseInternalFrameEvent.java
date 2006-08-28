package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * Represents an internal frame being shown.
 * @author Vivek Prahlad
 */
public class CloseInternalFrameEvent extends AbstractFrankensteinEvent {
    private String title;

    public CloseInternalFrameEvent(String title) {
        this.title = title;
    }

    public String toString() {
        return "CloseInternalFrameEvent: " + title;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        try {
            finder.findInternalFrame(context, title).setClosed(true);
        } catch (PropertyVetoException e) {
            throw new RuntimeException("CloseInternalFrameEvent: closing vetoed", e);
        }
    }

    public String target() {
        return title;
    }
}

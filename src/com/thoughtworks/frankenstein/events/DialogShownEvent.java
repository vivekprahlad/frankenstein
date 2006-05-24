package com.thoughtworks.frankenstein.events;

import java.awt.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Represents an internal frame being shown.
 * @author Vivek Prahlad
 */
public class DialogShownEvent extends AbstractFrankensteinEvent {
    private String title;
    public static final String DIALOG_SHOWN_ACTION = "DialogShown";
    static final int DEFAULT_TIMEOUT = 10;

    public DialogShownEvent(String title) {
        this.title = title;
    }

    public String toString() {
        return "DialogShownEvent: " + title;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        try {
            context.waitForDialog(title, DEFAULT_TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String target() {
        return title;
    }
}

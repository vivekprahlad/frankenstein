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
        eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    public String toString() {
        return "DialogShownEvent: " + title;
    }

    public String target() {
        return title;
    }

    public void run() {
        try {
            context.waitForDialog(title, DEFAULT_TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

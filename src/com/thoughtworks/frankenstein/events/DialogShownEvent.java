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
    private int delay;
    public static final String DIALOG_SHOWN_ACTION = "DialogShown";

    public DialogShownEvent(String scriptLine) {
        delay = Integer.parseInt(scriptLine);
        eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    public String toString() {
        return "DialogShownEvent: " + delay;
    }

    public String target() {
        return String.valueOf(delay);
    }

    public String parameters() {
        return "";
    }

    public void run() {
        try {
            context.waitForDialog(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

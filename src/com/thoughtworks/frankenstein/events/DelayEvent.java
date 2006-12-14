package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.application.ThreadUtil;

/**
 * Delays a script for a specifed duration of time.
 * @author Vivek Prahlad
 */
public class DelayEvent extends AbstractFrankensteinEvent {
    private int duration = 100;

    public DelayEvent(String scriptLine) {
        duration = Integer.parseInt(scriptLine);
        executeInPlayerThread();
    }

    public String toString() {
        return "DelayEvent: " + duration;
    }

    public String target() {
        return String.valueOf(duration);
    }

    public String parameters() {
        return "";
    }

    public void run() {
        ThreadUtil.sleep(duration);
    }
}

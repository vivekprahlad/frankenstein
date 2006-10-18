package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.application.ThreadUtil;

/**
 *
 * @author vivek
 */
public class DelayEvent extends AbstractFrankensteinEvent {
    private int duration = 100;

    public DelayEvent(String scriptLine) {
        duration = Integer.parseInt(scriptLine);
        eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
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

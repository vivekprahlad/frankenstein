package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Understands executing events: depending on the strategy, events are executed either in the current thread
 * (which will usually be the player thread) or the Swing thread.
 * Exceptions encountered in the Swing thread are propogated.
 * @author vivek
 
 */
public abstract class EventExecutionStrategy {
    abstract void execute(Runnable runnable);
    public static final EventExecutionStrategy IN_PLAYER_THREAD = new EventExecutionStrategy() {
        void execute(Runnable runnable) {
            runnable.run();
        }
    };
    public static final EventExecutionStrategy IN_SWING_THREAD = new EventExecutionStrategy() {
        void execute(Runnable runnable) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };
}

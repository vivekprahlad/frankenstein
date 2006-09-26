package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Understands executing events
 *
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

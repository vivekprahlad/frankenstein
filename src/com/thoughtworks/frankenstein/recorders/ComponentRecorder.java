package com.thoughtworks.frankenstein.recorders;

/**
 * Understands listening to AWT component events.
 *
 * @author Vivek Prahlad
 */
public interface ComponentRecorder {
    void register();

    void unregister();
}

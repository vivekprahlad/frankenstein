package com.thoughtworks.frankenstein.recorders;

/**
 * Understands recording swing events.
 * @author Vivek Prahlad
 */
public interface Recorder extends EventRecorder {
    void start();
    void stop();
    void play();
    void reset();
}

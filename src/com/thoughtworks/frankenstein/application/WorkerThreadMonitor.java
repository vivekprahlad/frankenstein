package com.thoughtworks.frankenstein.application;

/**
 * Understands activity in worker threads.
 * @author Vivek Prahlad
 */
public interface WorkerThreadMonitor {
    public void start();
    public void waitForIdle();
}

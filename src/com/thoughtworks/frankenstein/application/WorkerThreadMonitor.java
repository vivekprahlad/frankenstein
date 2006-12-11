package com.thoughtworks.frankenstein.application;

/**
 * Understands activity in worker threads.
 * @author Vivek Prahlad
 */
public interface WorkerThreadMonitor {
    /**
     * Called at the start of a test run.
     */
    public void start();

    /**
     * Blocking call, that returns after worker thread activity ceases.
     */
    public void waitForIdle();
}

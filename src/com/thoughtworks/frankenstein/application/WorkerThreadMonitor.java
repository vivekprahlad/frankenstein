package com.thoughtworks.frankenstein.application;

/**
 * Monitors worker thread activity.
 *
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

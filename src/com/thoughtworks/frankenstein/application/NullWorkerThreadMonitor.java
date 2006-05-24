package com.thoughtworks.frankenstein.application;

/**
 * Default implementation: does nothing.
 * @author Vivek Prahlad
 */
public class NullWorkerThreadMonitor implements WorkerThreadMonitor{
    public void start() {
    }

    public void waitForIdle() {
    }
}

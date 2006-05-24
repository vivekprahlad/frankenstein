package com.thoughtworks.frankenstein.application;

/**
 * Implements a simple worker thread monitor. Uses polling.
 * @author Vivek Prahlad
 */
public class SimpleWorkerThreadMonitor implements WorkerThreadMonitor{
    private int initialThreadCount;

    public void start() {
        initialThreadCount = Thread.activeCount();
    }

    public void waitForIdle() {
        while (Thread.activeCount() > initialThreadCount) {
                ThreadUtil.sleep(100);
        }
    }
}

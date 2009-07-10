package com.thoughtworks.frankenstein.application;

import junit.framework.TestCase;

/**
 * Ensures behaviour of the worker thread monitor
 */
public class RegexWorkerThreadMonitorTest extends TestCase {

    public void testWaitsForThreadToComplete() {
        WorkerThreadMonitor monitor = new RegexWorkerThreadMonitor("MyWorker");
        monitor.start();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "MyWorker");
        thread.start();
        assertTrue(thread.isAlive());
        monitor.waitForIdle();
        assertFalse(thread.isAlive());
        assertFalse(thread.isInterrupted());
    }
}

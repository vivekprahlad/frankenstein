package com.thoughtworks.frankenstein.application;

import junit.framework.TestCase;

/**
 * Ensures behaviour of NullWorkerThreadMonitor
 */
public class NullWorkerThreadMonitorTest extends TestCase {
    public void testStartingMonitorIsNotABlockingCall() {
       new NullWorkerThreadMonitor().start();
    }

    public void testWaitForIdleIsNotABlockingCall() {
       new NullWorkerThreadMonitor().waitForIdle();
    }
}

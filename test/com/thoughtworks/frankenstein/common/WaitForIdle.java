package com.thoughtworks.frankenstein.common;

/**
 * Waits until the event queue is drained.
 */
public class WaitForIdle {

    public WaitForIdle() {
    }

    public void waitForIdle() {
        RobotFactory.getRobot().waitForIdle();
    }
}

package com.thoughtworks.frankenstein.common;

import java.awt.*;

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

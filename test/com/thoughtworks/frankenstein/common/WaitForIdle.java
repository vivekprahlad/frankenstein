package com.thoughtworks.frankenstein.common;

import java.awt.*;

/**
 * Waits until the event queue is drained.
 */
public class WaitForIdle {
    private Robot robot;

    public WaitForIdle() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitForIdle() {
        robot.waitForIdle();
    }
}

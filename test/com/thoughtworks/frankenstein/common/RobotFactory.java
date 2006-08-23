package com.thoughtworks.frankenstein.common;

import java.awt.*;

/**
 * Creates java.awt.Robots
 */
public class RobotFactory {
    private static Robot robot;

    public static Robot getRobot() {
        if (robot == null) try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        return robot;
    }

}

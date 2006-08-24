package com.thoughtworks.frankenstein.actions;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Base class for all mouse actions.
 */
public class AbstractMouseAction {

    protected void moveMouse(Component component, Robot robot) {
        Point point = component.getLocationOnScreen();
        robot.mouseMove(point.x + component.getWidth() / 2, point.y + component.getHeight() / 2);
    }

    protected void click(Robot robot, int button) {
        robot.mousePress(button);
        robot.mouseRelease(button);
    }

    protected void click(Robot robot) {
        click(robot, InputEvent.BUTTON1_MASK);
    }

    public int hashCode() {
        return getClass().getName().hashCode();
    }

    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }
}

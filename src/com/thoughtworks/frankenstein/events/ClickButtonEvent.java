package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.InputEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands recording button clicks.
 * @author Vivek Prahlad
 */
public class ClickButtonEvent extends AbstractFrankensteinEvent {
    private String buttonName;

    public ClickButtonEvent(String buttonName) {
        this.buttonName = buttonName;
    }

    public String toString() {
        return "ClickButtonEvent: " + buttonName;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        AbstractButton button =  (AbstractButton) finder.findComponent(context, buttonName);
        Point point = button.getLocationOnScreen();
        robot.mouseMove(point.x + button.getWidth() / 2, point.y + button.getHeight() / 2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public String target() {
        return buttonName;
    }
}

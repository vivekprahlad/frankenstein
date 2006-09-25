package com.thoughtworks.frankenstein.events;

import java.awt.*;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Starts a test run.
 * @author Vivek Prahlad
 */
public class StartTestEvent extends AbstractFrankensteinEvent {
    private String testName;

    public StartTestEvent(String testName) {
        this.testName = testName;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        scriptContext.startTest(testName);
    }

    public String toString() {
        return "StartTest: " + testName;
    }

    public String target() {
        return "";
    }

    public String scriptLine() {
        return action() + " \"" + testName + "\"";
    }

    public String parameters() {
        return testName;
    }
}

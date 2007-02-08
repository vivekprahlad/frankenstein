package com.thoughtworks.frankenstein.events;

/**
 * Starts a test run.
 *
 * @author Vivek Prahlad
 */
public class StartTestEvent extends AbstractFrankensteinEvent {
    private String testName;

    public StartTestEvent(String testName) {
        this.testName = testName;
        executeInPlayerThread();
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

    public void run() {
        scriptContext.startTest(testName);
    }
}

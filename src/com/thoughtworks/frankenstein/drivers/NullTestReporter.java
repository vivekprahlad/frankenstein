package com.thoughtworks.frankenstein.drivers;

import java.awt.*;

import com.thoughtworks.frankenstein.script.TestReporter;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Null Test Reporter
 */
public class NullTestReporter implements TestReporter {
    public void startTest(String testName) {
    }

    public void reportSuccess(FrankensteinEvent event) {
    }

    public void reportFailure(FrankensteinEvent event, Exception cause, Robot robot) {
    }

    public void finishTest() {
    }
}

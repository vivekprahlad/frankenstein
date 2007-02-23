package com.thoughtworks.frankenstein.drivers;

import java.awt.*;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Fails fast(Throws exception) when the execution of an event fails.
 *
 * @author Pavan
 * @author Prakash
 */
public class FailFastReporter implements TestReporter {
    public void startTest(String testName) {
    }

    public void reportSuccess(FrankensteinEvent event) {
    }

    public void reportFailure(FrankensteinEvent event, Exception cause, Robot robot) {
        throw new RuntimeException("Test failed.", cause);
    }

    public void finishTest() {
    }
}

package com.thoughtworks.frankenstein.script;

import java.io.IOException;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Test reporting interface.
 * @author Vivek Prahlad
 */
public interface TestReporter {
    void startTest(String testName);
    void reportSuccess(FrankensteinEvent event);
    void reportFailure(FrankensteinEvent event, Exception cause);
    String finishTest() throws IOException;
}

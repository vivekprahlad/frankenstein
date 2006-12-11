package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.script.TestReporter;

import java.util.List;

/**
 * Core script interface.
 * @author Vivek Prahlad
 */
public interface ScriptContext {
    void startTest(String testName);

    void play(List events);

    boolean isScriptPassed();
}

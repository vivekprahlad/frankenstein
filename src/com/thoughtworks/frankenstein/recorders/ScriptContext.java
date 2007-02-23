package com.thoughtworks.frankenstein.recorders;

import java.util.List;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Core script interface.
 *
 * @author Vivek Prahlad
 */
public interface ScriptContext {
    void startTest(String testName);

    void startMonitor();

    void play(List events);

    boolean isScriptPassed();

    void addScriptListener(ScriptListener listener);

    void removeScriptListener(ScriptListener listener);

    void play(FrankensteinEvent event);
}

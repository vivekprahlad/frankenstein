package com.thoughtworks.frankenstein.recorders;

/**
 * Notifies interested listeners when a test run completes
 *
 * @author vivek
 */
public interface ScriptListener {
    void scriptCompleted(boolean passed);

    void scriptStepStarted(int eventIndex);
}

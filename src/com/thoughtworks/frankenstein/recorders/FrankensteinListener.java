package com.thoughtworks.frankenstein.recorders;

/**
 * Notifies interested listeners about playback changes.
 * @author Vivek Prahlad
 */
public interface FrankensteinListener {
    void currentStepChanged(int currentStep);
    void eventListChanged();
}

package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.events.AbstractFrankensteinEvent;

/**
 * Dummy frankenstein event
 */
public class TestFrankensteinEvent extends AbstractFrankensteinEvent {
    public TestFrankensteinEvent(String line) {

    }

    public String target() {
        return null;
    }

    public void run() {
    }
}

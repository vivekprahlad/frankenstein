package com.thoughtworks.frankenstein.application;

import java.awt.*;

import com.thoughtworks.frankenstein.events.AbstractFrankensteinEvent;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Dummy frankenstein event
 */
public class TestFrankensteinEvent extends AbstractFrankensteinEvent {
    public TestFrankensteinEvent(String line) {

    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
    }

    public String target() {
        return null;
    }
}

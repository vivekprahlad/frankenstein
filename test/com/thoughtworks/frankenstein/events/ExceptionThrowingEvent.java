package com.thoughtworks.frankenstein.events;

import java.awt.*;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Throws an exception when the event is constructed.
 */
public class ExceptionThrowingEvent implements FrankensteinEvent {

    public ExceptionThrowingEvent(String scriptLine) {
        throw new IllegalArgumentException("Wrong args");
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
    }

    public String action() {
        return null;
    }

    public String target() {
        return null;
    }

    public String parameters() {
        return null;
    }

    public String scriptLine() {
        return null;
    }

    public void run() {
    }
}

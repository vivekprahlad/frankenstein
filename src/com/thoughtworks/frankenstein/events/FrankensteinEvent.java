package com.thoughtworks.frankenstein.events;

import java.awt.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Represents a frankenstein event. Events can execute either in the Swing thread (in case they manipulate swing components)
 * or in the player thread.
 * @see EventExecutionStrategy
 * @author Vivek Prahlad
 */
public interface FrankensteinEvent extends Runnable{
    FrankensteinEvent NULL = new AbstractFrankensteinEvent() {
        public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
            throw new UnsupportedOperationException("Cannot play back null events");
        }

        public String target() {
            return "NullTarget";
        }

        public void run() {
        }
    };
    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot);
    public void record(EventList list, FrankensteinEvent lastEvent);
    public String action();
    public String target();
    public String parameters();
    public String scriptLine();
}

package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.script.Script;

/**
 * Base class for all events
 * @author Vivek Prahlad
 */
public abstract class AbstractFrankensteinEvent implements FrankensteinEvent {
    private static final Map actionNameMap = new HashMap();
    private EventExecutionStrategy eventExecutionStrategy = EventExecutionStrategy.IN_SWING_THREAD;
    protected WindowContext context;
    protected ComponentFinder finder;
    protected ScriptContext scriptContext;
    protected Robot robot;

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (isSameTargetAs(lastEvent)) {
            list.replaceLastEvent(this);
        } else {
            list.addEvent(this);
        }
    }

    protected void executeInPlayerThread() {
        eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    protected boolean isSameTargetAs(FrankensteinEvent lastEvent) {
        return action().equals(lastEvent.action()) && target().equals(lastEvent.target());
    }

    public String action() {
        if (!actionNameMap.containsKey(getClass())) actionNameMap.put(getClass(), EventActionName.eventActionName(getClass()));
        return (String) actionNameMap.get(getClass());
    }

    public String scriptLine() {
        return (underscore(action()) + " " + quote(target()) +  (parameters().equals("") ? "": " , "+quote(escapeNewLines(parameters())))).replaceAll("\\s", " ").trim();
    }
    
    protected String quote(String input) {
        return "\"" + input  +"\"";
    }

    private String escapeNewLines(String string) {
        return string.replaceAll("\n", Script.NEW_LINE);
    }

    protected String underscore(String action) {
        return action.replaceAll("(\\w)([A-Z])", "$1_$2").toLowerCase();
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String parameters() {
        return "";
    }

    protected static String[] params(String scriptLine) {
        return scriptLine.split("\\s", 2);
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        this.context = context;
        this.finder = finder;
        this.scriptContext = scriptContext;
        this.robot = robot;
        eventExecutionStrategy.execute(this);
    }
}

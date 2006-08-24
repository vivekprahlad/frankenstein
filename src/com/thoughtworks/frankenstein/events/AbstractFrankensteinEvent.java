package com.thoughtworks.frankenstein.events;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Base class for all events
 * @author Vivek Prahlad
 */
public abstract class AbstractFrankensteinEvent implements FrankensteinEvent {
    private static final Map actionNameMap = new HashMap();

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (isSameTargetAs(lastEvent)) {
            list.replaceLastEvent(this);
        } else {
            list.addEvent(this);
        }
    }

    private boolean isSameTargetAs(FrankensteinEvent lastEvent) {
        return action().equals(lastEvent.action()) && target().equals(lastEvent.target());
    }

    public String action() {
        if (!actionNameMap.containsKey(getClass())) actionNameMap.put(getClass(), EventActionName.action(getClass()));
        return (String) actionNameMap.get(getClass());
    }

    public String scriptLine() {
        return (underscore(action()) + " \"" + target() + "\"" +  (parameters().equals("") ? "": " \"" + parameters() + "\"")).replaceAll("/s", " ").trim();
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
}

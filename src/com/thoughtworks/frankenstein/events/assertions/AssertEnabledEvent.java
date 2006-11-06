package com.thoughtworks.frankenstein.events.assertions;

import com.thoughtworks.frankenstein.events.AbstractFrankensteinEvent;

/**
 * Asserts whether a component is enabled on not
 * @author vivek
 */
public class AssertEnabledEvent extends AbstractFrankensteinEvent {
    private String component;
    private boolean enabled;

    public AssertEnabledEvent(String component, boolean enabled) {
        this.component = component;
        this.enabled = enabled;
    }

    public AssertEnabledEvent(String scriptLine) {
        this(params(scriptLine)[0], Boolean.parseBoolean(params(scriptLine)[1]));
    }

    public String target() {
        return component;
    }

    public String parameters() {
        return String.valueOf(enabled);
    }

    public String toString() {
        return "AssertEnabledEvent: " + component + ", " + String.valueOf(enabled);
    }

    public void run() {
        if (!finder.findComponent(context, component).isEnabled() == enabled) {
           throw new RuntimeException("Expected component " + component + " to be " + state(enabled) + " but was " + state(!enabled));
        }
    }

    private String state(boolean enabled) {
        return enabled ? "enabled" : "disabled";
    }
}

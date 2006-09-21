package com.thoughtworks.frankenstein.naming;

import java.awt.*;

/**
 * Understands matching components by name.
 * @author Vivek Prahlad
 */
public class ComponentNameMatchingRule implements ComponentMatchingRule {
    private String componentName;
    private Component matchingComponent;

    public ComponentNameMatchingRule(String componentName) {
        this.componentName = componentName;
    }

    public boolean matchAndContinue(Component component) {
        if (componentName.equals(component.getName()) && component.isShowing()) {
            matchingComponent = component;
            return false;//Stop searching when a match is found.
        } else {
            return true;
        }
    }

    public boolean hasNoMatches() {
        return matchingComponent == null;
    }

    public Component matchingComponent() {
        return matchingComponent;
    }
}

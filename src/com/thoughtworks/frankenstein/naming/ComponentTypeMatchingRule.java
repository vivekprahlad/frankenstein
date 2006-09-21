package com.thoughtworks.frankenstein.naming;

import java.util.ArrayList;
import java.awt.*;

/**
 * Matches all components of a certain type. Returns a list of matching components.
 * @author Vivek Prahlad
 */
public class ComponentTypeMatchingRule implements ComponentMatchingRule{
    private java.util.List matchingComponents = new ArrayList();
    private Class componentType;

    public ComponentTypeMatchingRule(Class componentType) {
        this.componentType = componentType;
    }

    private boolean isSupportedComponent(Class componentClass) {
        return componentType.isAssignableFrom(componentClass);
    }

    public boolean matchAndContinue(Component component) {
        if (isSupportedComponent(component.getClass())) {
            matchingComponents.add(component);
        }
        return true;
    }

    public java.util.List components() {
        return matchingComponents;
    }

    public boolean hasNoMatches() {
        return matchingComponents.isEmpty();
    }
}

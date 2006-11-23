package com.thoughtworks.frankenstein.naming;

import java.util.*;
import java.awt.*;


/**
 * Matches all components of a certain type. Returns a list of matching components.
 * @author Vivek Prahlad
 */
public class UnnamedComponentMatchingRule implements ComponentMatchingRule{
    private java.util.List matchingComponents = new ArrayList();
    private Class componentType;

    public UnnamedComponentMatchingRule(Class componentType) {
        this.componentType = componentType;
    }

    private boolean isSupportedComponent(Class componentClass) {
        return componentType.isAssignableFrom(componentClass);
    }

    public boolean matchAndContinue(Component component) {
        if (isSupportedComponent(component.getClass()) && (component.getName() == null || isOptionPaneButton(component))) {
            matchingComponents.add(component);
        }
        return true;
    }

    private boolean isOptionPaneButton(Component component) {
        return component.getName().equals("OptionPane.button");
    }

    public java.util.List unnamedComponents() {
        return matchingComponents;
    }

    public boolean hasMatches() {
        return false;
    }
}

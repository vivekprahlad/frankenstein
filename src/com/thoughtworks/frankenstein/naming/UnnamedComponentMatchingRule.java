package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import java.util.ArrayList;


/**
 * Matches all components of a certain type. Returns a list of matching components.
 *
 * @author Vivek Prahlad
 */
public class UnnamedComponentMatchingRule implements ComponentMatchingRule {
    private java.util.List matchingComponents = new ArrayList();
    private Class componentType;
    private int counter = 0;

    public UnnamedComponentMatchingRule(Class componentType) {
        this.componentType = componentType;
        this.counter = 1;
    }

    private boolean isSupportedComponent(Class componentClass) {
        return componentType.isAssignableFrom(componentClass);
    }

    public boolean matchAndContinue(Component component) {
        if (component.getName() != null && isSupportedComponent(component.getClass())) {
            updateComponentNamingCounter(component);
        }
        if (isSupportedComponent(component.getClass()) &&
                (component.getName() == null || isOptionPaneButton(component))) {
            matchingComponents.add(component);
        }
        return true;
    }

    private void updateComponentNamingCounter(Component component) {
        if (isNamedWithCounter(component)) {
            String[] splits = component.getName().split("_");
            counter = Math.max(Integer.parseInt(splits[1]), counter) + 1;
        }
    }

    private boolean isNamedWithCounter(Component component) {
        return component.getName().matches(type(component.getClass()) + "_(\\d+)");
    }

    protected String type(Class componentType) {
        String[] type = componentType.getName().split("\\.");
        return type[type.length - 1];
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

    public int counter() {
        return counter;
    }
}

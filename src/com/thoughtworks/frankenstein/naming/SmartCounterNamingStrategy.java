package com.thoughtworks.frankenstein.naming;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Iterator;

/**
 * Names components with a counter.
 * @author Vivek Prahlad
 */
public class SmartCounterNamingStrategy extends AbstractComponentNamingStrategy implements ComponentNamingStrategy {
    int counter = 1;
    private Container container;
    private Class componentClass;
    private NamingStrategy strategy;
    private List matchingComponents;

    public SmartCounterNamingStrategy(String prefix, Container container, Class componentClass, NamingStrategy strategy) {
        super(prefix);
        this.container = container;
        this.componentClass = componentClass;
        this.strategy = strategy;
        matchingComponents(componentClass, container);
    }

    private void matchingComponents(Class componentClass, Container container) {
        ComponentTypeMatchingRule rule = new ComponentTypeMatchingRule(componentClass);
        new ComponentHierarchyWalker().matchComponentsIn(container, rule);
        matchingComponents = rule.components();
    }

    public void name(Component component) {
        component.setName(prefix((JComponent) component) + type(component.getClass()) + "_" + counter++);
    }

    public void name() {
        counter = counter + findMaximumCounter();
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(componentClass);
        new ComponentHierarchyWalker().matchComponentsIn(container, rule);
        strategy.nameComponentsIn(rule.unnamedComponents(), this);
    }

    private int findMaximumCounter() {
        int maximumCounter = 0;
        for (Iterator iterator = matchingComponents.iterator(); iterator.hasNext();) {
            Component component = (Component) iterator.next();
            if (component.getName()!= null && component.isShowing()) {
                int ctr = counter(component.getName());
                if (ctr > maximumCounter)
                   maximumCounter = ctr;
            }
        }
        return maximumCounter;
    }

    private int counter(String name) {
        try {
            return Integer.parseInt(name.substring(name.lastIndexOf("_") + 1, name.length() - 1));
        } catch (NumberFormatException e) {
            return counter;
        }
    }
}

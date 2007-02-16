package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import java.util.List;

/**
 * Understands traversing components from a top level container.
 *
 * @author Vivek Prahlad
 */
public class ComponentHierarchyWalker {

    public ComponentMatchingRule matchComponentsIn(Container container, ComponentMatchingRule rule) {
        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (!rule.matchAndContinue(component)) {
                break;
            }
            if (component instanceof Container) {
                matchComponentsIn((Container) component, rule);
            }
        }
        return rule;
    }

    public boolean hasNoMatches(Container container, Class componentClass) {
        return !(matchComponentsIn(container, new ComponentTypeMatchingRule(componentClass)).hasMatches());
    }

    public List getMatchingComponents(Container container, Class componentClass) {
        ComponentTypeMatchingRule typeMatchingRule = new ComponentTypeMatchingRule(componentClass);
        matchComponentsIn(container, typeMatchingRule);
        return typeMatchingRule.getMatchingComponents();
    }
}

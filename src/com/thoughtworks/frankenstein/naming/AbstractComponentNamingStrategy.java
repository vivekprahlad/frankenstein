package com.thoughtworks.frankenstein.naming;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Base class for all component naming strategies.
 *
 * @author Vivek Prahlad
 */
public abstract class AbstractComponentNamingStrategy {
    private String prefix;

    protected AbstractComponentNamingStrategy(String prefix) {
        this.prefix = prefix;
    }

    protected String findPrefix(JComponent component) {
        if (component.getParent() == null || !(component.getParent() instanceof JComponent)) return "";
        JComponent parent = (JComponent) component.getParent();
        Border border = parent.getBorder();
        if (border != null && border instanceof TitledBorder) {
            TitledBorder titledBorder = (TitledBorder) border;
            return findPrefix(parent, titledBorder).replaceAll("\\s", "");
        } else {
            return findPrefix(parent).replaceAll("\\s", "");
        }
    }

    protected String prefix(JComponent component) {
        return prefix + findPrefix(component);
    }

    private String findPrefix(JComponent parent, TitledBorder titledBorder) {
        return prefix(parent) + titledBorder.getTitle() + ".";
    }

    protected String type(Class componentType) {
        String[] type = componentType.getName().split("\\.");
        return type[type.length - 1];
    }
}

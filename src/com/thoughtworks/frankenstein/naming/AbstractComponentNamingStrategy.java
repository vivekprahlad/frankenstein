package com.thoughtworks.frankenstein.naming;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Base class for all component naming strategies.
 * @author Vivek Prahlad
 */
public abstract class AbstractComponentNamingStrategy {

    protected String prefix(JComponent component) {
        if (component.getParent() == null || !(component.getParent() instanceof JComponent)) return "";
        JComponent parent = (JComponent) component.getParent();
        Border border = parent.getBorder();
        if (border != null && border instanceof TitledBorder) {
            TitledBorder titledBorder = (TitledBorder) border;
            return prefix(parent, titledBorder).replaceAll("\\s", "");
        } else {
            return prefix(parent).replaceAll("\\s", "");
        }
    }

    private String prefix(JComponent parent, TitledBorder titledBorder) {
        return prefix(parent) + titledBorder.getTitle() + ".";
    }

    protected String type(Class componentType) {
        String[] type = componentType.getName().split("\\.");
        return type[type.length-1];
    }
}

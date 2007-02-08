package com.thoughtworks.frankenstein.common;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands assigning logical names to components.
 */
public class ComponentName {

    public void clear(JComponent component) {
        component.setName(null);
        clear(component.getComponents());
    }

    public String componentName(Component component, NamingStrategy namingStrategy) {
        if (component.getName() == null || component.getName().equals("OptionPane.button")) {
            nameTopLevelWindowWith(component, namingStrategy);
        }
        return component.getName();
    }

    private void nameTopLevelWindowWith(Component comp, NamingStrategy namingStrategy) {
        Component rootPane = new RootPaneContainerFinder().findRootPane(comp);
        namingStrategy.nameComponentsIn((Container) rootPane);
    }

    private void clear(Component[] components) {
        for (int i = 0; i < components.length; i++) {
            components[i].setName(null);
            if (components[i] instanceof  Container) {
                clear((((Container) components[i]).getComponents()));
            }
        }
    }
}

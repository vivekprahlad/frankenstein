package com.thoughtworks.frankenstein.recorders;

import java.awt.*;

import com.thoughtworks.frankenstein.common.RootPaneContainerFinder;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Represents the name of a component.
 * @author Vivek Prahlad
 */
class ComponentName {

    String componentName(Component component, NamingStrategy namingStrategy) {
        if (component.getName() == null) nameTopLevelWindowWith(component, namingStrategy);
        return component.getName();
    }

    private void nameTopLevelWindowWith(Component comp, NamingStrategy namingStrategy) {
        Component rootPane = new RootPaneContainerFinder().findRootPane(comp);
        namingStrategy.nameComponentsIn((Container) rootPane);
    }

}

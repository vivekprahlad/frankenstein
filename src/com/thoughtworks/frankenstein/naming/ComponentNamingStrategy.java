package com.thoughtworks.frankenstein.naming;

import java.awt.*;

/**
 * Names components swing components using component.setName()
 * @author Vivek Prahlad
 */
public interface ComponentNamingStrategy {
    public void name(Component component, int counter);
}

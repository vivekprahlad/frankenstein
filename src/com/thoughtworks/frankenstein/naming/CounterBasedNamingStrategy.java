package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import javax.swing.*;

/**
 * Names components with a counter.
 * @author Vivek Prahlad
 */
public class CounterBasedNamingStrategy extends AbstractComponentNamingStrategy implements ComponentNamingStrategy {
    protected CounterBasedNamingStrategy(String prefix) {
        super(prefix);
    }

    public void name(Component component, int counter) {
        component.setName(prefix((JComponent) component) + type(component.getClass()) + "_"+counter++);
    }

}

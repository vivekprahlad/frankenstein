package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import javax.swing.*;

/**
 * Names components with a counter.
 * @author Vivek Prahlad
 */
public class CounterBasedNamingStrategy extends AbstractComponentNamingStrategy implements ComponentNamingStrategy {
    int counter = 1;

    public void name(Component component) {
        component.setName(prefix((JComponent) component) + type(component.getClass()) + "_" + counter++);
    }

}

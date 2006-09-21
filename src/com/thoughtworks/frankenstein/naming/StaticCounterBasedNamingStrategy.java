package com.thoughtworks.frankenstein.naming;

import javax.swing.*;
import java.awt.*;

/**
 * Names components with a counter.
 * @author Vivek Prahlad
 */
public class StaticCounterBasedNamingStrategy extends AbstractComponentNamingStrategy implements ComponentNamingStrategy {
    static int counter = 1;

    public void name(Component component) {
        component.setName(prefix((JComponent) component) + type(component.getClass()) + "_" + counter++);
    }

}

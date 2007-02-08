package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import java.util.Comparator;

/**
 * Understands comparing the positions of components.
 *
 * @author Vivek Prahlad
 */
public class ComponentPositionComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        if (o1 == o2) return 0;
        Component first = (Component) o1;
        Component second = (Component) o2;
        return first.getY() != second.getY() ? first.getY() - second.getY() : first.getX() - second.getX();
    }
}

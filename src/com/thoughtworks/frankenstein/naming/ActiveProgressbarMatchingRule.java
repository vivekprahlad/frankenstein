package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import javax.swing.*;

/**
 * Finds active progress bars
 */
public class ActiveProgressbarMatchingRule implements ComponentMatchingRule {
    private boolean matches = false;

    public boolean matchAndContinue(Component component) {
        return !(isProgressbar(component) && isCompleted(component));
    }

    private boolean isCompleted(Component component) {
        JProgressBar bar = (JProgressBar) component;
        if (bar.isVisible() && bar.getValue() < bar.getMaximum()) {
            matches = true;
            return true;
        }
        return false;
    }

    private boolean isProgressbar(Component component) {
        return component instanceof JProgressBar;
    }

    public boolean hasMatches() {
        return matches;
    }

    public boolean matches(Container container, ComponentHierarchyWalker walker) {
        matches = false;
        return walker.matchComponentsIn(container, this).hasMatches();
    }
}

package com.thoughtworks.frankenstein.naming;

import javax.swing.*;
import java.awt.*;

/**
 * Matches all components of a certain type. Returns a list of matching components.
 * @author Vivek Prahlad
 */
public class InternalFrameMatchingRule implements ComponentMatchingRule{
    private String title;
    private JInternalFrame frame;

    public InternalFrameMatchingRule(String title) {
        this.title = title;
    }

    private boolean isSupportedComponent(Class componentClass) {
        return JInternalFrame.class.isAssignableFrom(componentClass);
    }

    public boolean matchAndContinue(Component component) {
        if (isSupportedComponent(component.getClass())) {
            JInternalFrame frame = (JInternalFrame) component;
            if (title.equals(frame.getTitle())) {
                this.frame = frame;
                return false;
            }
        }
        return true;
    }

    public JInternalFrame matchingComponent() {
        return frame;
    }

    public boolean hasMatches() {
        return frame != null;
    }
}

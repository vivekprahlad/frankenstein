package com.thoughtworks.frankenstein.common;

import java.awt.*;
import javax.swing.*;

/**
 * Finds a root pane container.
 * @author Vivek Prahlad
 */
public class RootPaneContainerFinder {
    public Component findRootPane(Component focusOwner) {
        if (focusOwner == null) return null;
        if (focusOwner instanceof RootPaneContainer) return focusOwner;
        if (focusOwner.getParent()!=null) return findRootPane(focusOwner.getParent());
        return null;
    }
}

package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import javax.swing.*;

/**
 * Finds components
 * @author Vivek Prahlad
 */
public interface ComponentFinder {
    Component findComponent(WindowContext parent, String componentName);

    void setEditor(Component component);

    JMenuItem findMenuItem(WindowContext context, String path);

    Window findWindow(String title);

    JInternalFrame findInternalFrame(WindowContext windowContext, String title);

    JFileChooser fileChooser(WindowContext context);
}

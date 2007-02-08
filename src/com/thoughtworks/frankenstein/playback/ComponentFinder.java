package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import javax.swing.*;

/**
 * Finds components
 *
 * @author Vivek Prahlad
 */
public interface ComponentFinder {
    Component findComponent(WindowContext parent, String componentName);

    void setTableCellEditor(Component component);

    JMenuItem findMenuItem(WindowContext context, String path);

    Window findWindow(String title);

    JInternalFrame findInternalFrame(WindowContext windowContext, String title);

    JFileChooser findFileChooser(WindowContext context);

    JLabel findLabel(WindowContext context, String labelValue);

    void menuDisplayed(JPopupMenu jPopupMenu);

    void menuHidden();

    JDialog findDialog(String title);
}

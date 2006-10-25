package com.thoughtworks.frankenstein.playback;

import java.awt.*;

/**
 * Represents the active window.
 * @author Vivek Prahlad
 */
public interface WindowContext {
    Component activeWindow();

    Component activeTopLevelWindow();

    Component focusOwner();

    void waitForDialogOpening(String title, int timeout) throws InterruptedException;

    void waitForDialogClosing(String title, int timeout);

    void close();

    void closeAllDialogs();

    void addWindowContextListener(WindowContextListener listener);

    void removeWindowContextListener(WindowContextListener listener);
}

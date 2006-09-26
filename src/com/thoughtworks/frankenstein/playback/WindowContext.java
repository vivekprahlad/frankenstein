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

    void waitForDialog(String title, int timeout) throws InterruptedException;

    void close();
}

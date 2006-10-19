package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.RootPaneContainerFinder;
import com.thoughtworks.frankenstein.application.ThreadUtil;

/**
 * Understands the currently active window. The active window could be an internal frame or a dialog.
 *
 * @author Vivek Prahlad
 */
public class DefaultWindowContext implements PropertyChangeListener, WindowContext {
    private Component activeWindow;
    private String title;

    public DefaultWindowContext() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() != null) {
            setActiveWindow(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        }
    }

    protected synchronized void setActiveWindow(Component focusOwner) {
        activeWindow = rootPaneContainer(focusOwner);
        if (activeWindow instanceof JDialog) {
            JDialog dialog = (JDialog) activeWindow;
            if (title != null && title.equals(dialog.getTitle())) {
                notifyAll();
            }
        }
    }

    private Component rootPaneContainer(Component focusOwner) {
        return new RootPaneContainerFinder().findRootPane(focusOwner);
    }

    public Component activeWindow() {
        return activeWindow;
    }

    public Component activeTopLevelWindow() {
        if (activeWindow instanceof JInternalFrame) {
            return new RootPaneContainerFinder().findRootPane(activeWindow.getParent());
        }
        return activeWindow;
    }

    public Component focusOwner() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    }

    public synchronized void waitForDialogOpening(String title, int timeoutInSeconds) throws InterruptedException {
        if (isDialogOpen(title)) return;
        this.title = title;
        wait(timeoutInSeconds * 1000);
//        if (!isDialogOpen(title)) throw //
        this.title = null;
    }

    private boolean isDialogOpen(String title) {
        if (activeWindow instanceof JDialog) {
            JDialog dialog = (JDialog) activeWindow;
            return (title.equals(dialog.getTitle()));
        }
        return false;
    }

    public void waitForDialogClosing(String title, int timeout) {
        if (!isDialogOpen(title)) return;
        while (isDialogOpen(title)) {
            ThreadUtil.sleep(100);
        }
    }

    public void close() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("focusOwner", this);
    }
}

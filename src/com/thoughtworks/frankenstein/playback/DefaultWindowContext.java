package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.EventListenerList;

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
    private EventListenerList listenerList = new EventListenerList();

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
        if (isDialogOpen()) {
            fireDialogOpened();
            JDialog dialog = (JDialog) activeWindow;
            if (title != null && title.equals(dialog.getTitle())) {
                notifyAll();
            }
        }
    }

    private void fireDialogOpened() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == WindowContextListener.class) {
                ((WindowContextListener) listeners[i + 1]).dialogShown();
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
        if (!isDialogOpen(title)) throw new RuntimeException("Dialog with title:" + title + " not opened");
        this.title = null;
    }

    private boolean isDialogOpen(String title) {
        if (isDialogOpen()) {
            JDialog dialog = (JDialog) activeWindow;
            return (title.equals(dialog.getTitle()));
        }
        return false;
    }

    private boolean isDialogOpen() {
        return activeWindow instanceof JDialog;
    }

    public void waitForDialogClosing(String title, int timeout) {
        if (!isDialogOpen(title)) return;
        long currentTime = System.currentTimeMillis();
        while (isDialogOpen(title) && (System.currentTimeMillis() - currentTime < timeout * 1000)) {
            ThreadUtil.sleep(100);
        }
        if (isDialogOpen(title)) throw new RuntimeException("Dialog with title:" + title + " not closed");
    }

    public void close() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("focusOwner", this);
    }

    public void closeAllDialogs() {
        while (isDialogOpen()) {
            closeDialog((JDialog) activeWindow);
        }
    }

    public void addWindowContextListener(WindowContextListener listener) {
        listenerList.add(WindowContextListener.class, listener);

    }

    public void removeWindowContextListener(WindowContextListener listener) {
        listenerList.remove(WindowContextListener.class, listener);
    }

    private void closeDialog(final JDialog dialog) {
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
        while (activeWindow == dialog) {
            ThreadUtil.sleep(100);
        }
    }
}

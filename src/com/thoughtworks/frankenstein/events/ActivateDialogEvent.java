package com.thoughtworks.frankenstein.events;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Represents a top level frame being activated.
 *
 * @author Vivek Prahlad
 */
public class ActivateDialogEvent extends AbstractFrankensteinEvent {
    private String title;
    private WindowListener listener = new WindowListener();

    public ActivateDialogEvent(String title) {
        this.title = title;
        executeInPlayerThread();
    }

    public String target() {
        return title;
    }

    public synchronized void run() {
        final JDialog dialog = finder.findDialog(title);
        dialog.addWindowListener(listener);
        activateDialog(dialog);
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        } finally {
            dialog.removeWindowListener(listener);
        }
    }

    private void activateDialog(final JDialog dialog) {
        dialog.toFront();
        dialog.toFront();
    }

    public synchronized void dialogActivated() {
        notifyAll();
    }

    private class WindowListener extends WindowAdapter {

        public void windowActivated(WindowEvent e) {
            dialogActivated();
        }
    }
}

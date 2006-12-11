package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        this.eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
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
        Thread dialogToggleThread = new Thread(new Runnable() {
            public void run() {
                dialog.setVisible(false);
                dialog.setVisible(true);
            }
        });
        dialogToggleThread.start();
    }

    public synchronized void dialogActivated() {
        notifyAll();
    }

    private class WindowListener extends WindowAdapter {

        public void windowActivated(WindowEvent e) {
            dialogActivated();
            System.out.println("Focussed");
        }
    }
}

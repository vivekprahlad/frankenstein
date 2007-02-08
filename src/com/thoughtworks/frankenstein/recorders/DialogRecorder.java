package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.DialogClosedEvent;
import com.thoughtworks.frankenstein.events.DialogShownEvent;

/**
 * Understands recording dialog.
 *
 * @author Vivek Prahlad
 */
public class DialogRecorder implements ComponentRecorder, WindowListener, AWTEventListener {
    private EventRecorder recorder;

    public DialogRecorder(EventRecorder recorder) {
        this.recorder = recorder;
    }

    private String title(JDialog dialog) {
        return dialog.getTitle() == null ? "" : dialog.getTitle();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
        JDialog dialog = (JDialog) e.getSource();
        dialog.removeWindowListener(this);
        recorder.record(new DialogClosedEvent(title(dialog)));
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    private void dialogShown(JDialog dialog) {
        recorder.record(new DialogShownEvent(title(dialog)));
        dialog.addWindowListener(this);
    }

    public void eventDispatched(AWTEvent event) {
        if (WindowEvent.WINDOW_OPENED == event.getID() && event.getSource() instanceof JDialog) {
            dialogShown((JDialog) event.getSource());
        }
    }
}

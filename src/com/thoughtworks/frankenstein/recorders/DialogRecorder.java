package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.DialogShownEvent;
import com.thoughtworks.frankenstein.events.DialogClosedEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording dialog.
 * @author Vivek Prahlad
 */
public class DialogRecorder extends AbstractComponentRecorder implements WindowListener {

    public DialogRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JDialog.class);
    }

    void componentShown(Component component) {
        JDialog dialog = dialog(component);
        recorder.record(new DialogShownEvent(title(dialog)));
        dialog.addWindowListener(this);
    }

    private String title(JDialog dialog) {
        return dialog.getTitle() == null ? "" : dialog.getTitle();
    }

    private JDialog dialog(Component component) {
        return (JDialog) component;
    }

    void componentHidden(Component component) {
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
}

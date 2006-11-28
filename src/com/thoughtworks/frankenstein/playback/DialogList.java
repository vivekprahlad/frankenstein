package com.thoughtworks.frankenstein.playback;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

/**
 * Understands maintaining a list of dialogs.
 *
 * @author vivek
 */
public class DialogList  extends WindowAdapter implements AWTEventListener {
    private List dialogs = new ArrayList();

    public DialogList() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_EVENT_MASK);
    }

    public void eventDispatched(AWTEvent event) {
        if (event.getID() == WindowEvent.WINDOW_OPENED) {
            WindowEvent windowEvent = (WindowEvent) event;
            if (windowEvent.getWindow() instanceof JDialog) {
                addDialog((JDialog) windowEvent.getWindow());
            }
        }
    }

    private void addDialog(JDialog dialog) {
        dialog.addWindowListener(this);
        dialogs.add(dialog);
    }

    public JDialog findDialog(String title) {
        for (Iterator iterator = dialogs.iterator(); iterator.hasNext();) {
            JDialog dialog = (JDialog) iterator.next();
            if (MatchStrategy.matchValues(title(dialog), title)) {
                return dialog;
            }
        }
        throw new RuntimeException("Could not find dialog with title: " + title);
    }

    public String title(JDialog dialog) {
        return dialog.getTitle() == null ? "" : dialog.getTitle();
    }

    public void windowClosed(WindowEvent e) {
        dialogs.remove(e.getWindow());
        e.getWindow().removeWindowListener(this);
    }
}

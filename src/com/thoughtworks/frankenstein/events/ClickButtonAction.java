package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.WindowContextListener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Understands clicking on a button.
 * @author vivek
 */
public class ClickButtonAction implements WindowContextListener, ActionListener, Runnable {
    private Throwable exception;
    private AbstractButton button;
    private String text;
    private Icon icon;
    private Action action;
    private String command;
    private int mnemonic;

    public synchronized void execute(final AbstractButton button, WindowContext context) {
        context.addWindowContextListener(this);
        this.button = button;
        SwingUtilities.invokeLater(this);
        try {
            //Wait until one of two things happens:
            //   a) All action listeners are processed - in that case, the actionPerformed method will get called
            //   b) A dialog is shown (the button's action listener will block until the dialog is closed, so we
            //                         rely on the window context to notify us in case a dialog pops up after the click)
            wait();
        } catch (Exception e) {
            throw new RuntimeException("Error waiting for click button event");
        } finally {
            context.removeWindowContextListener(this);
        }
        if (exception != null) throw new RuntimeException(exception);
    }

    private void addActionListener(AbstractButton button) {
        saveButtonAttributes();
        ActionListener[] listeners = button.getActionListeners();
        removeAllActionListeners(listeners, button);
        //Add the ClickButtonAction actionListener to the front of the listener list,
        //so that it will then be the last to be notified.
        button.addActionListener(this);
        addAllActionListeners(listeners, button);
        restoreButtonAttributes();
    }

    private void saveButtonAttributes() {
        action = button.getAction();
        command = button.getActionCommand();
        icon = button.getIcon();
        text = button.getText();
        mnemonic = button.getMnemonic();
    }

    private synchronized void notifyException(Exception e) {
        exception = e;
        notifyAll();
    }

    private void addAllActionListeners(ActionListener[] listeners, AbstractButton button) {
        for (int i = 0; i < listeners.length; i++) {
            button.addActionListener(listeners[i]);
        }
    }

    private void removeAllActionListeners(ActionListener[] listeners, AbstractButton button) {
        for (int i = 0; i < listeners.length; i++) {
            button.removeActionListener(listeners[i]);
        }
    }

    private void removeActionListener(AbstractButton button) {
        button.removeActionListener(this);
    }

    public synchronized void actionPerformed(ActionEvent e) {
        notifyAll();
    }

    public synchronized void dialogShown() {
        notifyAll();
    }

    public void run() {
        try {
            addActionListener(button);
            button.doClick();
        } catch (Exception e) {
            notifyException(e);
        } finally {
            removeActionListener(button);
        }
    }

    private void restoreButtonAttributes() {
        if (action != null) button.setAction(action);
        button.setActionCommand(command);
        button.setIcon(icon);
        button.setText(text);
        button.setMnemonic(mnemonic);
    }
}

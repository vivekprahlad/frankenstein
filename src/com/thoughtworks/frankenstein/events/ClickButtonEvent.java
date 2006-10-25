package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.WindowContextListener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Understands recording button clicks.
 *
 * @author Vivek Prahlad
 */
public class ClickButtonEvent extends AbstractFrankensteinEvent implements ActionListener, WindowContextListener {
    private String buttonName;
    public static final String CLICK_BUTTON_ACTION = "ClickButton";
    private static final Object LOCK = new Object();

    public ClickButtonEvent(String buttonName) {
        this.buttonName = buttonName;
        this.eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    public String toString() {
        return "ClickButtonEvent: " + buttonName;
    }

    public String target() {
        return buttonName;
    }

    public synchronized void run() {
        context.addWindowContextListener(this);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AbstractButton button = (AbstractButton) finder.findComponent(context, buttonName);
                addActionListener(button);
                try {
                    button.doClick();
                } finally {
                    button.removeActionListener(ClickButtonEvent.this);
                }
            }
        });
        try {
            //Wait until one of two things happens:
            //   a) All action listeners are processed - in that case, the actionPerformed method will get called
            //   b) A dialog is shown (the button's action listener will block until the dialog is closed, so we
            //                         rely on the window context to notify us in case a dialog pops up after the click)
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error waiting for click button event");
        }
        context.removeWindowContextListener(this);
    }

    private void addActionListener(AbstractButton button) {
        ActionListener[] listeners = button.getActionListeners();
        removeAllActionListeners(listeners, button);
        //Add the ClickButtonEvent actionListener to the front of the listener list. It will then be the last to be notified.
        button.addActionListener(this);
        addAllActionListeners(listeners, button);
    }

    private void addAllActionListeners(ActionListener[] listeners, AbstractButton button) {
        for (int i = 0; i < listeners.length; i++) {
            ActionListener listener = listeners[i];
            button.addActionListener(listener);
        }
    }

    private void removeAllActionListeners(ActionListener[] listeners, AbstractButton button) {
        for (int i = 0; i < listeners.length; i++) {
            ActionListener listener = listeners[i];
            button.removeActionListener(listener);
        }
    }

    public synchronized void actionPerformed(ActionEvent e) {
        notifyAll();
    }

    public synchronized void dialogShown() {
        notifyAll();
    }
}

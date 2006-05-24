package com.thoughtworks.frankenstein.spikes;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * Figuring out how key events work.
 */
public class KeyEventSpike {
    public static void main(String[] args) {
        JFrame frame = new JFrame("testing");
        frame.getContentPane().add(new JTextField());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    System.out.println("e = " + e);
                }
                return false;
            }
        });
    }
}

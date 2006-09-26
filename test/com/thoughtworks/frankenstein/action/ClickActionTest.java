package com.thoughtworks.frankenstein.action;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.thoughtworks.frankenstein.actions.ClickAction;
import com.thoughtworks.frankenstein.common.RobotFactory;

/**
 * Ensures behaviour of ClickAction
 */
public class ClickActionTest extends TestCase {
    public void testWaitsForWindowToShowAfterClick() {
        JFrame frame = new JFrame();
        JButton button = new JButton("Click");
        final JDialog dialog = new JDialog(frame, "TestDialog");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(true);
            }
        });
        frame.getContentPane().add(button);
        frame.pack();
        frame.setVisible(true);
        assertTrue(frame.isShowing());
        new ClickAction().execute(button, RobotFactory.getRobot());
        assertTrue(dialog.isShowing());
        frame.dispose();
        dialog.dispose();
    }
}

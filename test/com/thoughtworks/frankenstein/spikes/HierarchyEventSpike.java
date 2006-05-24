package com.thoughtworks.frankenstein.spikes;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import javax.swing.*;

/**
 * Figuring out how hierarchy events work.
 */
public class HierarchyEventSpike {
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setSize(100,100);
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                if (event instanceof HierarchyEvent) {
                    HierarchyEvent he = (HierarchyEvent) event;
                    if (he.getSource() instanceof JButton) {
                        System.out.println("he = " + he);
                    }
                    if ((he.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED)!=0) {
                        if (he.getSource() instanceof JButton) {
                            JButton button = (JButton) he.getSource();
                            if (button.isShowing()) {
                                System.out.println("Button showing: " + button);
                            } else {
                                System.out.println("Button not showing: " + button);
                            }
                        }
                    }
                } else {
                    System.out.println("event = " + event);
                }
            }
        }, AWTEvent.HIERARCHY_EVENT_MASK | AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK);
        JButton button = new JButton("click");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(frame, "Test");
            }
        });
        frame.getContentPane().add(button);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

package com.thoughtworks.frankenstein.spikes;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseEventSpike {
    public static void main(String[] args) {
        JFrame test = new JFrame("Mouse Test");
        test.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("e = " + e);
            }

            public void mousePressed(MouseEvent e) {
                System.out.println("e = " + e);
            }

            public void mouseReleased(MouseEvent e) {
                System.out.println("e = " + e);
            }
        });
        test.setSize(200,200);
        test.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        test.setVisible(true);
    }
}
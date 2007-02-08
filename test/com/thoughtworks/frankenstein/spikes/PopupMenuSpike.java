package com.thoughtworks.frankenstein.spikes;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Figuring out how to record popup menus.
 */
public class PopupMenuSpike {
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setSize(100, 100);
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JDialog dialog = new JDialog(frame, "title");
                dialog.setSize(200, 200);
                dialog.setVisible(true);
                if (e.isPopupTrigger()) {
                    JPopupMenu menu = new JPopupMenu();
                    menu.add(new JMenuItem("abc"));
                    menu.add(new JMenuItem("abc"));
                    menu.show(frame, 20, 20);
                }
            }
        });
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                System.out.println("event = " + event);
            }
        }, AWTEvent.COMPONENT_EVENT_MASK);
        frame.setVisible(true);
    }
}

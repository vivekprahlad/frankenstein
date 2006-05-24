package com.thoughtworks.frankenstein.spikes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.DefaultWindowContext;

/**
 *  
 */
public class WindowContextSpike {
    public static void main(String[] args) {
        DefaultWindowContext context = new DefaultWindowContext();
        createFrame("Title1");
        createFrame("Title2");
        enumerateFrames();
    }

    private static void enumerateFrames() {
        Frame[] frames = Frame.getFrames();
        for (int i=0; i<frames.length; i++) {
            System.out.println("Frame: " + frames[i].getTitle());
        }
    }

    private static void createFrame(String title) {
        JFrame frame = new JFrame(title);
        JDesktopPane contentPane = new JDesktopPane();
        frame.setContentPane(contentPane);
        addWindow(frame);
        addWindow(frame);
        frame.setVisible(true);
    }

    private static void addWindow(final JFrame frame) {
        JInternalFrame comp = new JInternalFrame("abc", true, true, true, true);
        JButton button = new JButton("click");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(null, "Option Pane");
            }
        });
        comp.getContentPane().setLayout(new BoxLayout(comp.getContentPane(), BoxLayout.Y_AXIS));
        comp.getContentPane().add(new JTextField());
        comp.getContentPane().add(button);
        comp.getContentPane().add(new JSlider());
        frame.pack();
        comp.setVisible(true);
        comp.setSize(100,100);
        frame.getContentPane().add(comp);
    }
}

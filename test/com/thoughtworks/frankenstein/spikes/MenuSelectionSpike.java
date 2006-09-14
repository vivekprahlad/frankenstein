package com.thoughtworks.frankenstein.spikes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeListener;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.recorders.MenuNavigationRecorder;
import com.thoughtworks.frankenstein.recorders.Recorder;
import com.thoughtworks.frankenstein.recorders.ScriptListener;

/**
 *
 */
public class MenuSelectionSpike {
    public static void main(String[] args) {
        MenuNavigationRecorder recorder = new MenuNavigationRecorder(new Recorder() {
            public void record(FrankensteinEvent event) {
                System.out.println("event = " + event);
            }

            public void addChangeListener(ChangeListener listener) {
            }

            public void removeChangeListener() {
            }

            public void start() {
            }

            public void stop() {
            }

            public void play() {
            }

            public void reset() {
            }

            public void addScriptListener(ScriptListener listener) {
            }

            public void removeScriptListener(ScriptListener listener) {
            }

            public List eventList() {
                return null;
            }

            public void setEventList(List events) {
            }
        }, new DefaultNamingStrategy());
        recorder.register();
        final JFrame frame = new JFrame("test");
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(200,200);
        JMenuBar menubar = new JMenuBar();
        final JMenu menu = new JMenu("test");
        JMenuItem menuItem = new JMenuItem("next");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Boo");
            }
        });
        menu.add(menuItem);
        menubar.add(menu);
        JScrollPane comp = new JScrollPane();
        comp.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showPopup(e, menu, frame);

            }

            public void mousePressed(MouseEvent e) {
                showPopup(e, menu, frame);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e, menu, frame);
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        frame.add(comp);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private static void showPopup(MouseEvent e, JMenu menu, JFrame frame) {
        if (e.isPopupTrigger()) {
            JPopupMenu popmenu = new JPopupMenu("Test");
            popmenu.add(menu);
            popmenu.show(frame.getContentPane(), e.getX(), e.getY());
        }
    }
}

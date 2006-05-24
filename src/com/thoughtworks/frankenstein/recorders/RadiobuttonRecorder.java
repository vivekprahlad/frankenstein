package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.ClickRadioButtonEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording a radio button selection.
 * @author Vivek Prahlad
 */
public class RadiobuttonRecorder extends AbstractComponentRecorder implements ActionListener {
    public RadiobuttonRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JRadioButton.class);
    }

    void componentShown(Component component) {
        checkBox(component).addActionListener(this);
    }

    void componentHidden(Component component) {
        checkBox(component).removeActionListener(this);
    }

    private JRadioButton checkBox(Component component) {
        return (JRadioButton) component;
    }

    public void actionPerformed(ActionEvent e) {
        recorder.record(new ClickRadioButtonEvent(checkBoxName(e)));
    }

    private String checkBoxName(ActionEvent e) {
        String name = ((JRadioButton) e.getSource()).getText();
        return name == null ? "" : name;
    }
}

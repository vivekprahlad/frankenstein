package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.ClickCheckboxEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording a check box.
 * @author Vivek Prahlad
 */
public class CheckBoxRecorder extends AbstractComponentRecorder implements ActionListener {
    public CheckBoxRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JCheckBox.class);
    }

    void componentShown(Component component) {
        checkBox(component).addActionListener(this);
    }

    void componentHidden(Component component) {
        checkBox(component).removeActionListener(this);
    }

    private JCheckBox checkBox(Component component) {
        return (JCheckBox) component;
    }

    public void actionPerformed(ActionEvent e) {
        recorder.record(new ClickCheckboxEvent(componentName(checkBox(e)), checkBox(e).isSelected()));
    }

    private JCheckBox checkBox(ActionEvent e) {
        return (JCheckBox) e.getSource();
    }
}

package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.thoughtworks.frankenstein.events.CheckTextEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Records check text events
 * @author Vivek Prahlad
 */
public class CheckTextRecorder extends AbstractCheckRecorder {

    public CheckTextRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JTextComponent.class);
    }

    protected void check(Component source) {
        JTextField component = (JTextField) source;
        recorder.record(new CheckTextEvent(componentName(component), component.getText()));
    }
}

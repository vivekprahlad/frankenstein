package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.common.ComponentName;

import javax.swing.*;
import java.awt.*;

public class SpinnerRecorder extends AbstractComponentRecorder {
    public SpinnerRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JSpinner.class);
    }

    void componentShown(Component component) {
        new ComponentName().clear(spinner(component).getEditor());
    }

    private JSpinner spinner(Component component) {
        return (JSpinner) component;
    }

    void componentHidden(Component component) {
    }
}




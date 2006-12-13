package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.events.MoveSliderEvent;
import com.thoughtworks.frankenstein.common.ComponentName;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.*;
import java.awt.*;

/**
    Understands recording slider motion
 */
public class MoveSliderRecorder extends AbstractComponentRecorder implements ChangeListener {
    public MoveSliderRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JSlider.class);
    }

    void componentShown(Component component) {
        slider(component).addChangeListener(this);
    }

    private JSlider slider(Component component) {
        return((JSlider)component);
    }

    void componentHidden(Component component) {
        slider(component).removeChangeListener(this);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider slider= (JSlider) e.getSource();
        if(!slider.getValueIsAdjusting()) {
            recorder.record(new MoveSliderEvent(componentName(slider),slider.getValue()));
        }
    }
}
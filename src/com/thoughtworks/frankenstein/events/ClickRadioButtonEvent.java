package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands selecting checkboxes.
 * @author Vivek Prahlad
 */
public class ClickRadioButtonEvent extends AbstractFrankensteinEvent {
    private String radioButtonName;

    public ClickRadioButtonEvent(String checkBoxName) {
        this.radioButtonName = checkBoxName;
    }

    public String toString() {
        return "ClickRadioButtonEvent: " + radioButtonName;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        JRadioButton radioButton = (JRadioButton) finder.findComponent(context, radioButtonName);
        radioButton.doClick();
    }

    public String target() {
        return radioButtonName;
    }
}

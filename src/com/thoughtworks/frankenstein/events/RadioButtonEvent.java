package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.events.actions.Action;

import javax.swing.*;


/**
 * Understands radio button actio
 * @author Vivek Prahlad
 */
public class RadioButtonEvent extends AbstractCompoundEvent {
    private String radioButtonName;

    public RadioButtonEvent(String checkBoxName, Action action) {
        super(action);
        this.radioButtonName = checkBoxName;
    }

    public String toString() {
        return "RadioButtonEvent: " + radioButtonName;
    }

    public String target() {
        return radioButtonName;
    }

    public void run() {
        JRadioButton radioButton = (JRadioButton) finder.findComponent(context, radioButtonName);
        action.execute(center(radioButton), radioButton, finder, context);
    }
}

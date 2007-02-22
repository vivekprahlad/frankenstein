package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import com.thoughtworks.frankenstein.events.actions.Action;


/**
 * Understands radio button actio
 *
 * @author Vivek Prahlad
 */
public class RadioButtonEvent extends AbstractCompoundEvent {
    private String radioButtonName;

    public RadioButtonEvent(String radioButtonName, Action action) {
        super(action);
        this.radioButtonName = radioButtonName;
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

package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.events.actions.Action;

import javax.swing.*;


/**
 * Understands selecting checkboxes.
 *
 * @author Vivek Prahlad
 */
public class CheckboxEvent extends AbstractCompoundEvent {
    private String checkBoxName;
    private boolean selected;

    public CheckboxEvent(String checkBoxName, boolean isSelected, Action action) {
        super(action);
        this.checkBoxName = checkBoxName;
        selected = isSelected;
    }

    public CheckboxEvent(String scriptLine, Action action) {
        this(checkBoxParams(scriptLine)[0], Boolean.valueOf(checkBoxParams(scriptLine)[1]).booleanValue(), action);
    }

    private static String[] checkBoxParams(String scriptLine) {
        return replace(scriptLine).split("@");
    }

    protected static String replace(String line) {
        return line.replaceAll("(\\s)(true|false)(\\s?)", "@$2");
    }

    public String toString() {
        return "CheckboxEvent: " + checkBoxName + ", selected: " + selected;
    }

    public String target() {
        return checkBoxName;
    }

    public String parameters() {
        return String.valueOf(selected);
    }

    public void run() {
        JCheckBox checkBox = (JCheckBox) finder.findComponent(context, checkBoxName);
        action.execute(center(checkBox), checkBox, finder, context);
    }
}

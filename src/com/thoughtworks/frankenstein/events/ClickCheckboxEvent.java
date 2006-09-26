package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands selecting checkboxes.
 *
 * @author Vivek Prahlad
 */
public class ClickCheckboxEvent extends AbstractFrankensteinEvent {
    private String checkBoxName;
    private boolean selected;

    public ClickCheckboxEvent(String checkBoxName, boolean isSelected) {
        this.checkBoxName = checkBoxName;
        selected = isSelected;
    }

    public ClickCheckboxEvent(String scriptLine) {
        this(checkBoxParams(scriptLine)[0], Boolean.valueOf(checkBoxParams(scriptLine)[1]).booleanValue());
    }

    private static String[] checkBoxParams(String scriptLine) {
        return replace(scriptLine).split("@");
    }

    protected static String replace(String line) {
        return line.replaceAll("(\\s)(true|false)(\\s?)", "@$2");
    }

    public String toString() {
        return "ClickCheckboxEvent: " + checkBoxName + ", selected: " + selected;
    }

    public String target() {
        return checkBoxName;
    }

    public String parameters() {
        return String.valueOf(selected);
    }

    public void run() {
        JCheckBox checkBox = (JCheckBox) finder.findComponent(context, checkBoxName);
        if (checkBox.isSelected() ^ selected) {
            checkBox.doClick();
        }
    }
}

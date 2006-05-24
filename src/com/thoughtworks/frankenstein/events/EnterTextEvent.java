package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands entering text in a text field.
 * @author Vivek Prahlad
 */
public class EnterTextEvent extends AbstractFrankensteinEvent {
    private String textFieldName;
    private String text;

    public EnterTextEvent(String textFieldName, String text) {
        this.textFieldName = textFieldName;
        this.text = text;
    }

    public EnterTextEvent(String scriptLine) {
        this(params(scriptLine)[0], params(scriptLine)[1]);
    }

    public String toString() {
        return "EnterTextEvent: " + "TextField: "+ textFieldName + ", text: "+ text;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        JTextField field = (JTextField) finder.findComponent(context, textFieldName);
        field.setText(text);
    }

    public String target() {
        return textFieldName;
    }

    public String parameters() {
        return text;
    }
}

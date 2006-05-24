package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Checks text event
 * @author Vivek Prahlad
 */
public class CheckTextEvent extends AbstractFrankensteinEvent {
    private String textField, text;

    public CheckTextEvent(String textField, String text) {
        this.textField = textField;
        this.text = text;
    }

    public CheckTextEvent(String args) {
        this(params(args)[0], params(args)[1]);
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        JTextField field = (JTextField) finder.findComponent(context, textField);
        if (!text.equals(field.getText())) throw new RuntimeException("Expected \"" + text + "\" but was \""+ field.getText() + "\"");
    }

    public String target() {
        return textField;
    }

    public String parameters() {
        return text;
    }
}

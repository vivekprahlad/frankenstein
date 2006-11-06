package com.thoughtworks.frankenstein.events.assertions;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.events.AbstractFrankensteinEvent;

/**
 * Assert text event
 * @author Vivek Prahlad
 */
public class AssertTextEvent extends AbstractFrankensteinEvent {
    private String textField, text;

    public AssertTextEvent(String textField, String text) {
        this.textField = textField;
        this.text = text;
    }

    public AssertTextEvent(String args) {
        this(params(args)[0], params(args)[1]);
    }

    public String target() {
        return textField;
    }

    public String parameters() {
        return text;
    }

    public void run() {
        JTextField field = (JTextField) finder.findComponent(context, textField);
        if (!text.equals(field.getText())) throw new RuntimeException("Expected \"" + text + "\" but was \""+ field.getText() + "\"");
    }
}

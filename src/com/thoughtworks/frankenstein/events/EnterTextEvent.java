package com.thoughtworks.frankenstein.events;

import javax.swing.text.JTextComponent;

/**
 * Understands entering text in a text field.
 *
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
        return "EnterTextEvent: " + "TextField: " + textFieldName + ", text: " + text;
    }

    public String target() {
        return textFieldName;
    }

    public String parameters() {
        return text;
    }

    public void run() {
        JTextComponent field = (JTextComponent) finder.findComponent(context, textFieldName);
        field.setText(text);
    }
}

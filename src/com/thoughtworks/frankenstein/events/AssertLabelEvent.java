package com.thoughtworks.frankenstein.events;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 */
public class AssertLabelEvent extends AbstractFrankensteinEvent {
    private String labelValue;

    public AssertLabelEvent(String labelValue) {
        this.labelValue = labelValue;
    }

    public String toString() {
        return "AssertLabelEvent: " + labelValue;
    }

    public String parameters() {
        return labelValue;
    }

    public String target() {
        return "";
    }

    public void run() {
        try {
            finder.findLabel(context, labelValue);
        } catch (Exception e) {
            Logger.getLogger("Frankenstein").log(Level.WARNING, "AssertLabel failed: Could not find label with text " + labelValue);
        }
    }
}


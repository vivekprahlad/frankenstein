package com.thoughtworks.frankenstein.events;

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


    public String scriptLine() {
        return underscore(action()) + SPACE + quote(parameters());
    }

    public void run() {
        finder.findLabel(context, labelValue);
    }
}


package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.MatchStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cprakash
 * Date: Nov 22, 2006
 * Time: 3:53:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssertLabelEvent extends AbstractFrankensteinEvent {
    private String labelValue;
    private String label;

    public AssertLabelEvent( String label,String labelValue) {
        this.labelValue = labelValue;
        this.label=label;
    }

     public AssertLabelEvent(String scriptLine) {
        this(params(scriptLine)[0],params(scriptLine)[1]);
    }
    public String toString() {
        return "AssertLabelEvent: " + labelValue;
    }

    public String parameters() {
        return labelValue;
    }

    public String target() {
        return "label";
    }

    public void run() {
        finder.findLabel(context, labelValue);
    }
}


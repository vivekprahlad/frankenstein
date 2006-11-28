package com.thoughtworks.frankenstein.naming;

import com.thoughtworks.frankenstein.playback.MatchStrategy;

import javax.swing.*;
import java.awt.*;

/**
 */
public class LabelMatchingRule implements ComponentMatchingRule {
    private Component matchingLabel;
    private String componentValue;

    public LabelMatchingRule(String componentValue) {
        this.componentValue = componentValue;
    }

    public boolean matchAndContinue(Component component) {
        if (!(component instanceof JLabel)) return true;
        String realValue = ((JLabel) component).getText();
        if (MatchStrategy.matchValues(realValue, componentValue)) {
            matchingLabel = component;
            return false;
        } else {
            return true;
        }
    }

    public boolean hasMatches() {
        return matchingLabel != null;
    }

    public JLabel matchingLabel() {
        return (JLabel) matchingLabel;
    }
}

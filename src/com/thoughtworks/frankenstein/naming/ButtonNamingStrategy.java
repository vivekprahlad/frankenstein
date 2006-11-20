package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import javax.swing.*;

/**
 * Names components with a counter.
 * @author Vivek Prahlad
 */
public class ButtonNamingStrategy extends AbstractComponentNamingStrategy implements ComponentNamingStrategy {
    private int counter = 1;
    public void name(Component component) {
        if (!(component instanceof AbstractButton)) throw new IllegalArgumentException("Can only be used to name buttons");
        component.setName(prefix((JComponent) component) + buttonName(component).replaceAll("\\s+", ""));
    }

    private String buttonName(Component component) {
        AbstractButton button = (AbstractButton) component;
        return isButtonTextEmpty(button) ? iconName(button) : button.getText();
    }

    private String iconName(AbstractButton button) {
        return button.getIcon() == null ? String.valueOf(counter++) : simpleIconName(button.getIcon().toString());
    }

    protected String simpleIconName(String iconPath) {
        return iconPath.replaceAll("(.*)jar!(.*)", "$2").replaceAll("^/", "").replaceAll(".*/(.*)\\..*", "$1");
    }

    private boolean isButtonTextEmpty(AbstractButton button) {
        return button.getText() == null || "".equals(button.getText());
    }

}

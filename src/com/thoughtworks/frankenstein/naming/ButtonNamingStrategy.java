package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import javax.swing.*;

/**
 * Names components with a counter.
 * @author Vivek Prahlad
 */
public class ButtonNamingStrategy extends AbstractComponentNamingStrategy implements ComponentNamingStrategy {

    protected ButtonNamingStrategy(String prefix) {
        super(prefix);
    }

    public void name(Component component, int counter) {
        if (!(component instanceof AbstractButton)) throw new IllegalArgumentException("Can only be used to name buttons");
        component.setName(prefix((JComponent) component) + buttonName(component, counter).replaceAll("\\s+", ""));
    }

    private String buttonName(Component component, int counter) {
        AbstractButton button = (AbstractButton) component;
        return isButtonTextEmpty(button) ? iconName(button, counter) : button.getText();
    }

    private String iconName(AbstractButton button, int counter) {
        return button.getIcon() == null ? String.valueOf(counter++) : simpleIconName(button.getIcon().toString());
    }

    protected String simpleIconName(String iconPath) {
        return iconPath.replaceAll("(.*)jar!(.*)", "$2").replaceAll("^/", "").replaceAll(".*/(.*)\\..*", "$1");
    }

    private boolean isButtonTextEmpty(AbstractButton button) {
        return button.getText() == null || "".equals(button.getText());
    }

}

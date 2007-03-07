package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import javax.swing.*;

/**
 * Names checkboxes
 */
public class CheckBoxNamingStrategy extends AbstractComponentNamingStrategy implements ComponentNamingStrategy {

    public CheckBoxNamingStrategy(String prefix) {
        super(prefix);
    }


    public void name(Component component, int counter) {
        JCheckBox checkBox = (JCheckBox) component;
        if (checkBox.getText()!=null) name(checkBox, checkBox.getText());
        if (checkBox.getParent() instanceof JTable) {
            JTable table = (JTable) checkBox.getParent();
            name(checkBox, table.getColumnName(table.getEditingColumn()));
        }
    }

    private void name(JCheckBox checkBox, String text) {
        checkBox.setName(removeWhitespace(text));
    }
}

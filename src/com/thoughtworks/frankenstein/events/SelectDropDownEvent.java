package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.DefaultComponentDecoder;

/**
 * Represents a combo box selection event.
 * @author Vivek Prahlad
 */
public class SelectDropDownEvent extends AbstractFrankensteinEvent {
    private String comboBoxName;
    private String choice;

    public SelectDropDownEvent(String comboBoxName, String choice) {
        this.comboBoxName = comboBoxName;
        this.choice = choice;
    }

    public SelectDropDownEvent(String scriptLine) {
        this(params(scriptLine)[0], params(scriptLine)[1]);
    }

    public String toString() {
        return "SelectDropDownEvent: " + "Combo: " + comboBoxName + ", Choice: " + choice;
    }

    public String target() {
        return comboBoxName;
    }

    public String parameters() {
        return choice;
    }

    private String valueToString(ListCellRenderer renderer, Object element, int i) {
        Component rendererComponent = renderer.getListCellRendererComponent(new JList(), element, i, true, true);
        return new DefaultComponentDecoder().decode(rendererComponent);
    }

    public void run() {
        JComboBox combo = (JComboBox) finder.findComponent(context, comboBoxName);
        ListCellRenderer renderer = combo.getRenderer();
        ComboBoxModel model = combo.getModel();
        Object selectValue=null;
        for (int i=0; i< model.getSize(); i++) {
            Object element = model.getElementAt(i);
            if (valueToString(renderer, element, i).matches(choice)) {
                selectValue = element;
                break;
            }
        }
        combo.setSelectedItem(selectValue);
    }
}

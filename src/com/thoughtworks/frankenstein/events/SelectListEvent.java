package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.DefaultComponentDecoder;

/**
 * Represents a List selection event.
 * @author Vivek Prahlad
 */
public class SelectListEvent extends AbstractFrankensteinEvent {
    private String listName;
    private String choice;

    public SelectListEvent(String listName, String element) {
        this.listName = listName;
        this.choice = element;
    }

    public SelectListEvent(String scriptLine) {
        this(params(scriptLine)[0], params(scriptLine)[1]);
    }

    public String toString() {
        return "SelectListEvent: List: " + listName + ", Value: " + choice;
    }

    public String target() {
        return listName;
    }

    public String parameters() {
        return choice;
    }

    private Object value(JList list) {
        ListModel model = list.getModel();
        for (int i=0; i<model.getSize(); i++) {
            if (choice.equals(valueAsString(list, model.getElementAt(i), i)))
                return model.getElementAt(i);
        }
        return null;
    }

    private Object valueAsString(JList list, Object elementAt, int i) {
        ListCellRenderer renderer = list.getCellRenderer();
        Component component = renderer.getListCellRendererComponent(list, elementAt, i, true, true);
        return new DefaultComponentDecoder().decode(component);
    }

    public void run() {
        JList list = (JList) finder.findComponent(context, listName);
        list.setSelectedValue(value(list), true);
    }
}

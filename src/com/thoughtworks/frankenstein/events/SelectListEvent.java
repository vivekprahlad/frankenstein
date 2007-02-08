package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.DefaultComponentDecoder;
import com.thoughtworks.frankenstein.playback.MatchStrategy;

/**
 * Represents a List selection event.
 * @author Vivek Prahlad
 */
public class SelectListEvent extends AbstractFrankensteinEvent {
    private String listName;
    private String[] choices;
    private Map valueToString = new HashMap();

    public SelectListEvent(String listName, String[] elements) {
        this.listName = listName;
        this.choices = elements;
    }

    public SelectListEvent(String scriptLine) {
        this(params(scriptLine)[0], params(scriptLine)[1].split("\\|"));
    }

    public String toString() {
        return "SelectListEvent: List: " + listName + ", Values: " + parameters();
    }

    public String target() {
        return listName;
    }

    public String parameters() {
        String params = "";
        for (int i = 0; i < choices.length; i++) {
            String s = choices[i];
            params += s + ",";
        }
        return params.substring(0, params.length()-1);
    }

    private int value(JList list, String text) {
        ListModel model = list.getModel();
        for (int i=0; i<model.getSize(); i++) {
            if (MatchStrategy.matchValues(valueAsString(list, model.getElementAt(i), i),text))
                return i;
        }
        return -1;
    }

    private String valueAsString(JList list, Object elementAt, int i) {
        if (valueToString.containsKey(elementAt)) return (String) valueToString.get(elementAt);
        ListCellRenderer renderer = list.getCellRenderer();
        Component component = renderer.getListCellRendererComponent(list, elementAt, i, true, true);
        String string = new DefaultComponentDecoder().decode(component);
        valueToString.put(elementAt, string);
        return string;
    }

    public void run() {
        valueToString.clear();
        JList list = (JList) finder.findComponent(context, listName);
        list.setSelectedIndices(indices(list));
    }

    private int[] indices(JList list) {
        int[] indices = new int[choices.length];
        for (int i = 0; i < choices.length; i++) {
            indices[i] = value(list, choices[i]);
        }
        return indices;
    }
}

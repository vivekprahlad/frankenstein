package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thoughtworks.frankenstein.common.ComponentDecoder;
import com.thoughtworks.frankenstein.events.SelectListEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Records list selection events.
 * @author Vivek Prahlad
 */
public class ListSelectionRecorder extends AbstractComponentRecorder implements ListSelectionListener {
    private ComponentDecoder decoder;
    private ComponentVisibility visibility;

    public ListSelectionRecorder(EventRecorder recorder, NamingStrategy namingStrategy, ComponentDecoder decoder, ComponentVisibility visibility) {
        super(recorder, namingStrategy, JList.class);
        this.decoder = decoder;
        this.visibility = visibility;
    }

    void componentShown(Component component) {
        list(component).addListSelectionListener(this);
    }

    private JList list(Component component) {
        return (JList) component;
    }

    void componentHidden(Component component) {
        list(component).removeListSelectionListener(this);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        JList list = (JList) e.getSource();
        if (!visibility.isShowing(list) || list.getClass().getName().matches(".*Combo.*") || isFileChooserChild(list)) return;
        recorder.record(new SelectListEvent(componentName(list), values(list)));
    }

    private String[] values(JList list) {
        Object[] selectedValues = list.getSelectedValues();
        String[] values = new String[selectedValues.length];
        for (int i = 0; i < values.length; i++) {
            Component rendererComponent = list.getCellRenderer().getListCellRendererComponent(list, selectedValues[i],
                    list.getSelectedIndices()[i], false, false);
            values[i] = decoder.decode(rendererComponent);
        }
        return values;
    }

    private boolean isFileChooserChild(JList list) {
        Component parent = list.getParent();
        while(parent  != null) {
            if (parent instanceof JFileChooser) return true;
            parent = parent.getParent();
        }
        return false;
    }
}

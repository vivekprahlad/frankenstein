package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.ComponentDecoder;
import com.thoughtworks.frankenstein.events.SelectDropDownEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording item listeners.
 *
 * @author Vivek Prahlad
 */
public class SelectDropDownRecorder extends AbstractComponentRecorder implements ItemListener {
    private ComponentDecoder decoder;
    private static JList DUMMY_LIST = new JList();
    private ComponentVisibility visibility;

    public SelectDropDownRecorder(EventRecorder recorder, NamingStrategy namingStrategy, ComponentDecoder decoder, ComponentVisibility visibility) {
        super(recorder, namingStrategy, JComboBox.class);
        this.decoder = decoder;
        this.visibility = visibility;
    }

    void componentShown(Component component) {
        combo(component).addItemListener(this);
    }

    void componentHidden(Component component) {
        combo(component).removeItemListener(this);
    }

    private JComboBox combo(Component component) {
        return (JComboBox) component;
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox combo = (JComboBox) e.getSource();
            if (!visibility.isShowingAndHasFocus(combo)) return;
            ListCellRenderer renderer = combo.getRenderer();
            Component rendererComponent = renderer.getListCellRendererComponent(DUMMY_LIST, combo.getSelectedItem(), combo.getSelectedIndex(), false, false);
            recorder.record(new SelectDropDownEvent(componentName(combo), decoder.decode(rendererComponent)));
        }
    }
}

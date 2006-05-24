package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thoughtworks.frankenstein.events.SwitchTabEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording switch events
 * @author Vivek Prahlad
 */
public class TabSwitchRecorder extends AbstractComponentRecorder implements ChangeListener {
    private ComponentVisibility visibility;

    public TabSwitchRecorder(EventRecorder recorder, NamingStrategy namingStrategy, ComponentVisibility visibility) {
        super(recorder, namingStrategy, JTabbedPane.class);
        this.visibility = visibility;
    }

    void componentShown(Component component) {
        tabbedPane(component).addChangeListener(this);
    }

    void componentHidden(Component component) {
        tabbedPane(component).removeChangeListener(this);
    }

    private JTabbedPane tabbedPane(Component component) {
        return (JTabbedPane) component;
    }

    public void stateChanged(ChangeEvent e) {
        if (!visibility.isShowing(tabbedPane(e))) return;
        recorder.record(new SwitchTabEvent(componentName(tabbedPane(e)), tabbedPane(e).getTitleAt(tabbedPane(e).getSelectedIndex())));
    }

    private JTabbedPane tabbedPane(ChangeEvent e) {
        return (JTabbedPane) e.getSource();
    }
}

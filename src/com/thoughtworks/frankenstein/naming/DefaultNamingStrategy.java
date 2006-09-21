package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

/**
 * Understands naming components.
 * @author Vivek Prahlad
 */
public class DefaultNamingStrategy implements NamingStrategy {

    public void nameComponentsIn(Container panel) {
        nameComponentOfType(JTextField.class, panel, new CounterBasedNamingStrategy());
        nameComponentOfType(JTextArea.class, panel, new CounterBasedNamingStrategy());
        nameComponentOfType(JRadioButton.class, panel, new ButtonNamingStrategy());
        nameComponentOfType(JCheckBox.class, panel, new ButtonNamingStrategy());
        nameComponentOfType(JToggleButton.class, panel, new ButtonNamingStrategy());
        nameComponentOfType(JButton.class, panel, new ButtonNamingStrategy());
        nameComponentOfType(JList.class, panel, new CounterBasedNamingStrategy());
        nameComponentOfType(JComboBox.class, panel, new CounterBasedNamingStrategy());
        nameComponentOfType(JTable.class, panel, new CounterBasedNamingStrategy());
        nameComponentOfType(JTabbedPane.class, panel, new StaticCounterBasedNamingStrategy());
        nameComponentOfType(JTree.class, panel, new CounterBasedNamingStrategy());
    }

    private void nameComponentOfType(Class componentType, Container panel, ComponentNamingStrategy strategy) {
        List components = matchedComponents(componentType, new ComponentHierarchyWalker(), panel);
        Collections.sort(components, new ComponentPositionComparator());
        nameComponentsIn(components, strategy);
    }

    private List matchedComponents(Class componentType, ComponentHierarchyWalker componentHierarchyWalker, Container panel) {
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(componentType);
        componentHierarchyWalker.matchComponentsIn(panel, rule);
        return rule.unnamedComponents();
    }

    private void nameComponentsIn(List components, ComponentNamingStrategy strategy) {
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            strategy.name((Component) iterator.next());
        }
    }

}

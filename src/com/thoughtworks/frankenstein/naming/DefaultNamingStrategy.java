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
        nameComponentsIn("", panel);
    }

    private void nameComponentOfType(Class componentType, Container panel, ComponentNamingStrategy strategy) {
        List components = matchedComponents(componentType, new ComponentHierarchyWalker(), panel);
        nameComponentsIn(components, strategy);
    }

    private List matchedComponents(Class componentType, ComponentHierarchyWalker componentHierarchyWalker, Container panel) {
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(componentType);
        componentHierarchyWalker.matchComponentsIn(panel, rule);
        return rule.unnamedComponents();
    }

    public void nameComponentsIn(List components, ComponentNamingStrategy strategy) {
        Collections.sort(components, new ComponentPositionComparator());
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            strategy.name((Component) iterator.next());
        }
    }

    public void nameComponentsIn(String prefix, Container panel) {
        nameComponentOfType(JSpinner.class, panel, new SpinnerNamingStrategy(prefix, this));
        nameComponentOfType(JTextField.class, panel, new CounterBasedNamingStrategy(prefix));
        nameComponentOfType(JTextArea.class, panel, new CounterBasedNamingStrategy(prefix));
        nameComponentOfType(JRadioButton.class, panel, new ButtonNamingStrategy(prefix));
        nameComponentOfType(JCheckBox.class, panel, new ButtonNamingStrategy(prefix));
        nameComponentOfType(JToggleButton.class, panel, new ButtonNamingStrategy(prefix));
        nameComponentOfType(JButton.class, panel, new ButtonNamingStrategy(prefix));
        nameComponentOfType(JList.class, panel, new CounterBasedNamingStrategy(prefix));
        nameComponentOfType(JComboBox.class, panel, new CounterBasedNamingStrategy(prefix));
        nameComponentOfType(JTable.class, panel, new CounterBasedNamingStrategy(prefix));
        new SmartCounterNamingStrategy(prefix, panel, JTabbedPane.class, this).name();
        nameComponentOfType(JTree.class, panel, new CounterBasedNamingStrategy(prefix));
    }

}

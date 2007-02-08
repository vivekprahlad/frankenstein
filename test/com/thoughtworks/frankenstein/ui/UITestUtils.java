package com.thoughtworks.frankenstein.ui;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.naming.ComponentHierarchyWalker;
import com.thoughtworks.frankenstein.naming.UnnamedComponentMatchingRule;
import junit.framework.Assert;

public class UITestUtils {
    JFrame frame;

    public UITestUtils(JFrame frame) {
        this.frame = frame;
    }

    public UITestUtils(Component component) {
        frame = new JFrame();
        frame.getContentPane().add(component);
    }

    public AbstractButton getButtonAndAssertName(Class componentType, int index, String name) {
        AbstractButton button = (AbstractButton) getComponent(index, componentType);
        Assert.assertEquals(name, button.getText());
        return button;
    }

    public JComponent getComponent(int index, Class componentType) {
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(componentType);
        new ComponentHierarchyWalker().matchComponentsIn(frame, rule);
        return (JComponent) rule.unnamedComponents().get(index);
    }

    public JRadioButton getAndAssertRadioButton(int index, String name) {
        return (JRadioButton) getButtonAndAssertName(JRadioButton.class, index, name);
    }

    public JButton getAndAssertJButton(int index, String name) {
        return (JButton) getButtonAndAssertName(JButton.class, index, name);
    }
}

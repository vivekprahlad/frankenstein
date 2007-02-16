package com.thoughtworks.frankenstein.naming;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import junit.framework.TestCase;

/**
 * Ensures behaviour of the component list
 */
public class ComponentHierarchyWalkerTest extends TestCase {
    private ComponentHierarchyWalker hierarchyWalker;

    protected void setUp() throws Exception {
        super.setUp();
        hierarchyWalker = new ComponentHierarchyWalker();
    }

    public void testFindsComponentFromTopLevel() {
        JPanel panel = createPanel();
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JTextComponent.class);
        hierarchyWalker.matchComponentsIn(panel, rule);
        assertEquals(4, rule.unnamedComponents().size());
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.add(new JTextField());
        panel.add(new JTextField());
        panel.add(new JTextArea());
        panel.add(Box.createHorizontalBox());
        panel.add(new JTextField());
        return panel;
    }

    public void testFindsComponentsRecursively() {
        JPanel panel = createPanel();
        panel.add(createPanel());
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JTextComponent.class);
        hierarchyWalker.matchComponentsIn(panel, rule);
        assertEquals(8, rule.unnamedComponents().size());
    }

    public void testMatchesComponentsByName() throws InterruptedException, InvocationTargetException {
        JPanel panel = new JPanel();
        panel.add(new JTextField());
        panel.add(new JTextField());
        JTextArea namedTextArea = createTextArea();
        namedTextArea.setName("testName");
        panel.add(namedTextArea);
        ComponentNameMatchingRule rule = new ComponentNameMatchingRule("testName");
        hierarchyWalker.matchComponentsIn(panel, rule);
        assertSame(namedTextArea, rule.matchingComponent());
    }

    public void testGetMatchingComponents() {
        JPanel panel = createPanel();
        JPanel innerPanel = new JPanel();
        innerPanel.add(new JTextField());
        panel.add(innerPanel);
        List matchingComponents = hierarchyWalker.getMatchingComponents(panel, JTextField.class);
        assertEquals(4, matchingComponents.size());
        assertTrue(matchingComponents.get(0) instanceof JTextField);
        assertTrue(matchingComponents.get(1) instanceof JTextField);
        assertTrue(matchingComponents.get(2) instanceof JTextField);
    }

    private JTextArea createTextArea() throws InvocationTargetException, InterruptedException {
        final JTextArea[] namedTextArea = new JTextArea[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                namedTextArea[0] = new JTextArea() {
                    public boolean isShowing() {
                        return true;
                    }
                };
            }
        });
        return namedTextArea[0];
    }

    public void testHasNoMatches() {
        assertFalse(hierarchyWalker.matchComponentsIn(new JPanel(), new ComponentNameMatchingRule("testName")).hasMatches());
    }
}

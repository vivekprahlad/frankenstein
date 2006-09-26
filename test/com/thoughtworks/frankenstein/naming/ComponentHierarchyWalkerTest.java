package com.thoughtworks.frankenstein.naming;

import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.lang.reflect.InvocationTargetException;

/**
 * Ensures behaviour of the component list
 */
public class ComponentHierarchyWalkerTest extends TestCase {
    private ComponentHierarchyWalker factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new ComponentHierarchyWalker();
    }

    public void testFindsComponentFromTopLevel() {
        JPanel panel = createPanel();
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JTextComponent.class);
        factory.matchComponentsIn(panel, rule);
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
        factory.matchComponentsIn(panel, rule);
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
        factory.matchComponentsIn(panel, rule);
        assertSame(namedTextArea, rule.matchingComponent());
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
        assertTrue(factory.matchComponentsIn(new JPanel(), new ComponentNameMatchingRule("testName")).hasNoMatches());
    }
}

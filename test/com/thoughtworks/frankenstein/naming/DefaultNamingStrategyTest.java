package com.thoughtworks.frankenstein.naming;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import javax.swing.table.JTableHeader;

import junit.framework.TestCase;

/**
 * Understands naming components
 */
public class DefaultNamingStrategyTest extends TestCase {
    private DefaultNamingStrategy namingStrategy;

    protected void setUp() throws Exception {
        super.setUp();
        namingStrategy = new DefaultNamingStrategy();
    }

    public void testNamesTextFields() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField first = new JTextField();
        panel.add(first);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("JTextField_1", first.getName());
    }

    public void testNamesComponentsWithTitledBorderAsPrefix() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Title"));
        JTextField first = new JTextField();
        panel.add(first);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("Title.JTextField_1", first.getName());
    }

    public void testNamesComponentsNestedInTitledBorder() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Title"));
        JPanel secondLevelNestedPanel = new JPanel();
        JTextField first = new JTextField();
        secondLevelNestedPanel.add(first);
        JPanel nestedPanel = new JPanel();
        nestedPanel.add(secondLevelNestedPanel);
        panel.add(nestedPanel);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("Title.JTextField_1", first.getName());
    }

    public void testNamesComponentsNestedInMultipleTitledBorders() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createTitledBorder("TopLevelTitle"));
        JPanel secondLevelNestedPanel = new JPanel();
        secondLevelNestedPanel.setBorder(BorderFactory.createTitledBorder("NextLevelTitle"));
        JTextField first = new JTextField();
        secondLevelNestedPanel.add(first);
        JPanel nestedPanel = new JPanel();
        nestedPanel.add(secondLevelNestedPanel);
        panel.add(nestedPanel);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("TopLevelTitle.NextLevelTitle.JTextField_1", first.getName());
    }

    public void testNamesRadioButtons() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JRadioButton first = new JRadioButton("one");
        panel.add(first);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("one", first.getName());
    }

    public void testNamesRadioButtonsWithImageIcons() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        ImageIcon icon = new ImageIcon("icons/list-add.png");
        JRadioButton first = new JRadioButton(icon);
        panel.add(first);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("list-add", first.getName());
    }

    public void testNamesTrees() {
        JTree tree = new JTree();
        JPanel panel = new JPanel();
        panel.add(tree);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("JTree_1", tree.getName());
    }

    public void testNamesJTabbedPaneNestedInATabbedPane() throws InterruptedException, InvocationTargetException {
        final JTabbedPane parent = createTabbedPane();
        parent.setName("JTabbedPane_1");
        final JTabbedPane child = new JTabbedPane();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                parent.add(child);
            }
        });
        JPanel container = new JPanel();
        container.add(parent);
        namingStrategy.nameComponentsIn(container);
        assertEquals("JTabbedPane_2", child.getName());
    }

    private JTabbedPane createTabbedPane() throws InvocationTargetException, InterruptedException {
        final JTabbedPane[] tabbedPane = new JTabbedPane[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                tabbedPane[0] = new JTabbedPane() {
                    public boolean isShowing() {
                        return true;
                    }
                };
            }
        });
        ;
        return tabbedPane[0];
    }

    public void testNamesJTabbedPaneNestedInATabbedPaneWhenBothParentsAreNotNamed() {
        JPanel container = new JPanel();
        JTabbedPane parent = new JTabbedPane();
        JTabbedPane child = new JTabbedPane();
        parent.add(child);
        container.add(parent);
        namingStrategy.nameComponentsIn(container);
        assertEquals("JTabbedPane_2", child.getName());
        assertEquals("JTabbedPane_1", parent.getName());
    }

    public void testNamesJTabbedPane() {
        JPanel container = new JPanel();
        JTabbedPane parent = new JTabbedPane();
        container.add(parent);
        namingStrategy.nameComponentsIn(container);
        assertEquals("JTabbedPane_1", parent.getName());
    }

    public void testNamesOptionpaneButtons() {
        JPanel container = new JPanel();
        JButton optionPaneButton = new JButton("Yes");
        optionPaneButton.setName("OptionPane.button");
        container.add(optionPaneButton);
        namingStrategy.nameComponentsIn(container);
        assertEquals("Yes", optionPaneButton.getName());
    }

    public void testDoesNotNameSpinnerWhenAlreadyNamed() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JSpinner first = new JSpinner();
        first.setName("one");
        panel.add(first);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("one", first.getName());
    }

    public void testNamesSpinnersWhenNotNamed() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JSpinner first = new JSpinner();
        panel.add(first);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("JSpinner_1", first.getName());
    }

    public void testNamesTextFieldsInSpinner() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JSpinner spinner = new JSpinner();
        panel.add(spinner);
        namingStrategy.nameComponentsIn(panel);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        assertEquals("JSpinner_1.JFormattedTextField_1", editor.getTextField().getName());
    }

    public void testNamesTextFieldsInTwoSpinnersDifferently() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JSpinner firstSpinner = new JSpinner();
        JSpinner secondSpinner = new JSpinner();
        panel.add(firstSpinner);
        panel.add(secondSpinner);
        namingStrategy.nameComponentsIn(panel);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) firstSpinner.getEditor();
        assertEquals("JSpinner_1.JFormattedTextField_1", editor.getTextField().getName());
        editor = (JSpinner.DefaultEditor) secondSpinner.getEditor();
        assertEquals("JSpinner_2.JFormattedTextField_1", editor.getTextField().getName());
    }

    public void testNamesArbitrarySpinnerEditors() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JSpinner spinner = new JSpinner();
        spinner.setEditor(new JTextField());
        panel.add(spinner);
        namingStrategy.nameComponentsIn(panel);
        JTextField field = (JTextField) spinner.getEditor();
        assertEquals("JSpinner_1.JTextField_1", field.getName());
    }

    public void testNamesTableHeader() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTableHeader header = new JTableHeader();
        panel.add(header);
        namingStrategy.nameComponentsIn(panel);
        assertEquals("JTableHeader_1", header.getName());
    }
}

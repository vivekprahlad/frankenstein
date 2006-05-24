package com.thoughtworks.frankenstein.playback;

import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of component finder.
 */
public class DefaultComponentFinderTest extends MockObjectTestCase {
    private DefaultComponentFinder finder;
    private Mock mockWindowContext;
    private JMenuItem menuItem;

    protected void setUp() throws Exception {
        super.setUp();
        finder = new DefaultComponentFinder(new DefaultNamingStrategy());
        mockWindowContext = mock(WindowContext.class);
    }

    public void testFindsComponentsByName() {
        JPanel panel = new JPanel();
        JTextField textField = new JTextField();
        textField.setName("parent.textfield");
        panel.add(textField);
        mockWindowContext.expects(once()).method("activeWindow").will(returnValue(panel));
        assertSame(textField, finder.findComponent((WindowContext) mockWindowContext.proxy(), "parent.textfield"));
    }

    public void testDoesNotProceedIfComponentNotFound() {
        mockWindowContext.expects(atLeastOnce()).method("activeWindow").will(returnValue(new JPanel()));
        try {
            finder.findComponent((WindowContext) mockWindowContext.proxy(), "parent.textfield");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFindWindowByName() {
        JFrame frame = new JFrame("testFrame");
        frame.setVisible(true);
        assertSame(frame, finder.findWindow("testFrame"));
        frame.dispose();
    }

    public void testFailsIfThereIsNoWindowWithSuppliedTitle() {
        try {
            finder.findWindow("nonexistent");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFindMenuItem() {
        JFrame frame = new JFrame("testFrame");
        frame.getContentPane().add(createMenuBar());
        frame.setVisible(true);
        assertSame(menuItem, finder.findMenuItem(null, "Top>Next>Third"));
        frame.setVisible(false);
        frame.dispose();
    }

    public void testFailsIfTopLevelMenuNotFound() {
        try {
            finder.findMenuItem(null, "abc>wrong");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFailsIfMenuItemNotFound() {
        try {
            finder.findMenuItem(null, "Top>Next>Wrong");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFaislIfNonExistedMenuPathSupplied() {
        try {
            finder.findMenuItem(null, "path>doesnt>exist");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFindsEditorComponent() {
        JTextField component = new JTextField();
        component.setName("table.editor");
        finder.setEditor(component);
        assertSame(component, finder.findComponent(null, "table.editor"));
    }

    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Top");
        JMenu nextMenu = new JMenu("Next");
        menuItem = new JMenuItem("Third");
        nextMenu.add(menuItem);
        menu.add(nextMenu);
        menubar.add(menu);
        return menubar;
    }
}

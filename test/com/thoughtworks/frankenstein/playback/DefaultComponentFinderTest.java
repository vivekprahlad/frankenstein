package com.thoughtworks.frankenstein.playback;

import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

import java.lang.reflect.InvocationTargetException;

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

    public void testFindsComponentsByName() throws InterruptedException, InvocationTargetException {
        JPanel panel = new JPanel();
        JTextField textField = createTextField();
        textField.setName("parent.textfield");
        panel.add(textField);
        mockWindowContext.expects(once()).method("activeWindow").will(returnValue(panel));
        assertSame(textField, finder.findComponent((WindowContext) mockWindowContext.proxy(), "parent.textfield"));
    }

    private JTextField createTextField() throws InvocationTargetException, InterruptedException {
        final JTextField[] textField = new JTextField[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                textField[0] = new JTextField() {
                    public boolean isShowing() {
                        return true;
                    }
                };
            }
        });
        ;
        return textField[0];
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

    public void testFindsMenuItemFromMenuBars() {
        JFrame frame = new JFrame("testFrame");
        frame.getContentPane().add(createMenuBar());
        frame.setVisible(true);
        assertSame(menuItem, finder.findMenuItem(null, "Top>Next>Third"));
        frame.setVisible(false);
        frame.dispose();
    }

    public void testFindsPopupMenuItems() {
        JPopupMenu menu = new JPopupMenu("Test");
        menu.add(createMenu("Top"));
        finder.menuDisplayed(menu);
        assertSame(menuItem, finder.findMenuItem(null, "Top>Next>Third"));
    }

    public void testThrowsExceptionIfPopupMenuIsNotFound() {
        JPopupMenu menu = new JPopupMenu("Test");
        menu.add(createMenu("Top"));
        finder.menuDisplayed(menu);
        try {
            finder.findMenuItem(null, "Bogus>Next>Third");
            fail("Should have thrown an exception");
        } catch (Exception e) {
            //Expected
        }
    }

    public void testFindsMenuItemFromFramesAfterPopupIsHidden() {
        JPopupMenu menu = new JPopupMenu("Test");
        menu.add(createMenu("TopPopup"));
        finder.menuDisplayed(menu);
        finder.menuHidden();
        JFrame frame = new JFrame("testFrame");
        frame.getContentPane().add(createMenuBar());
        frame.setVisible(true);
        assertSame(menuItem, finder.findMenuItem(null, "Top>Next>Third"));
        frame.setVisible(false);
        frame.dispose();
    }

    public void testNotifiesWhenMenuItemIsDisplayed() {
        fail("Implement this test");
    }

    public void testFailsIfTopLevelMenuIsNotFound() {
        try {
            finder.findMenuItem(null, "abc>wrong");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFailsIfMenuItemIsNotFound() {
        try {
            finder.findMenuItem(null, "Top>Next>Wrong");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFaislIfNonExistedMenuPathIsSupplied() {
        try {
            finder.findMenuItem(null, "path>doesnt>exist");
            fail();
        } catch (Exception e) {
        }
    }

    public void testFindsEditorComponent() {
        JTextField component = new JTextField();
        component.setName("table.editor");
        finder.setTableCellEditor(component);
        assertSame(component, finder.findComponent(null, "table.editor"));
    }

    public void testFindsInternalFrame() {
        JFrame frame = new JFrame("testFrame");
        JDesktopPane pane = new JDesktopPane();
        JInternalFrame internalFrame = new JInternalFrame("Test");
        pane.add(internalFrame);
        frame.setContentPane(pane);
        frame.setVisible(true);
        mockWindowContext.expects(once()).method("activeTopLevelWindow").will(returnValue(frame));
        assertSame(internalFrame, finder.findInternalFrame((WindowContext) mockWindowContext.proxy(), "Test"));
        frame.dispose();
    }

    public void testFindsFileChooser() {
        JFileChooser chooser = new JFileChooser(".");
        JFrame frame = new JFrame();
        frame.getContentPane().add(chooser);
        mockWindowContext.expects(once()).method("activeWindow").will(returnValue(frame));
        assertSame(chooser, finder.findFileChooser((WindowContext) mockWindowContext.proxy()));
    }

    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = createMenu("Top");
        menubar.add(menu);
        return menubar;
    }

    private JMenu createMenu(String top) {
        JMenu menu = new JMenu(top);
        JMenu nextMenu = new JMenu("Next");
        menuItem = new JMenuItem("Third");
        nextMenu.add(menuItem);
        menu.add(nextMenu);
        return menu;
    }
}

package com.thoughtworks.frankenstein.playback;

import java.lang.reflect.InvocationTargetException;
import java.awt.*;
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

    protected void tearDown() throws Exception {
        finder.menuHidden();
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

    public void testFindsWindowWithRegularExpression() {
        JFrame frame = new JFrame("testRegexFrame");
        frame.setVisible(true);
        assertSame(frame, finder.findWindow("regex:testR.*"));
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
        frame.getContentPane().add(createMenuBar("Down"));
        frame.getContentPane().add(createMenuBar("Top"));
        frame.setVisible(true);
        assertSame(menuItem, finder.findMenuItem(null, "Top>Next>Third"));
        frame.setVisible(false);
        frame.dispose();
    }

    public void testFindsMenuItemFromMultipleFrames() {
        JFrame frameOne = new JFrame("one");
        frameOne.getContentPane().add(createMenuBar("FirstWindow"));
        frameOne.setVisible(true);
        JFrame frameTwo = new JFrame("two");
        frameTwo.getContentPane().add(createMenuBar("SecondWindow"));
        frameTwo.setVisible(true);
        assertSame(menuItem, finder.findMenuItem(null, "SecondWindow>Next>Third"));
        frameOne.setVisible(false);
        frameOne.dispose();
        frameTwo.setVisible(false);
        frameTwo.dispose();
    }

    public void testFindsMenuItemsInMenusWithSeparators() {
        JFrame frame = new JFrame("testFrame");
        JMenuBar menubar = createMenubarWithSeparatorAndMenuItem();
        frame.getContentPane().add(menubar);
        frame.setVisible(true);
        assertSame(this.menuItem, finder.findMenuItem(null, "One>Two"));
        frame.setVisible(false);
        frame.dispose();
    }

    private JMenuBar createMenubarWithSeparatorAndMenuItem() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("One");
        menu.addSeparator();
        menuItem = new JMenuItem("Two");
        menu.add(menuItem);
        menubar.add(menu);
        return menubar;
    }

    public void testFindsNestedMenuItemsInMenusWithSeparators() {
        JFrame frame = new JFrame("testFrame");
        JMenuBar menubar = createMenuBarWithNestedMenuItems();
        frame.getContentPane().add(menubar);
        frame.setVisible(true);
        assertSame(this.menuItem, finder.findMenuItem(null, "One>Two>Three"));
        frame.setVisible(false);
        frame.dispose();
    }

    private JMenuBar createMenuBarWithNestedMenuItems() {
        JMenuBar menubar = new JMenuBar();
        JMenu one = new JMenu("One");
        one.addSeparator();
        JMenu two = new JMenu("Two");
        two.addSeparator();
        menuItem = new JMenuItem("Three");
        one.add(two);
        two.add(menuItem);
        menubar.add(one);
        return menubar;
    }

    public void testFindsPopupMenuItems() {
        JPopupMenu menu = new JPopupMenu("Test");
        menu.add(createMenu("Top"));
        finder.menuDisplayed(menu);
        assertSame(menuItem, finder.findMenuItem(null, "Top>Next>Third"));
    }

    public void testClearsPopupMenuOnceFound() {
        //This is a temporary hack until we find a better way to clear the popup menu
        JPopupMenu menu = new JPopupMenu("Test");
        menu.add(createMenu("Popup"));
        finder.menuDisplayed(menu);
        assertSame(menuItem, finder.findMenuItem(null, "Popup>Next>Third"));
        try {
            finder.findMenuItem(null, "Popup>Next>Third");
            fail("Should not have been able to find the menu item again");
        } catch (Exception e) {
            //Expected.
        }
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

    public void testFindsLabel() {
        JLabel label = new JLabel("label");
        label.setVisible(true);
        JPanel panel = new JPanel();
        panel.add(label);
        mockWindowContext.expects(once()).method("activeWindow").will(returnValue(panel));
        assertSame(label, finder.findLabel((WindowContext) mockWindowContext.proxy(), "label"));
    }

    public void testThrowsExceptionWhenTheRequiredLabelIsNotFound() {
        JLabel label = new JLabel("dummyLabel");
        label.setVisible(true);
        JPanel panel = new JPanel();
        panel.add(label);
        mockWindowContext.expects(once()).method("activeWindow").will(returnValue(panel));
        try {
            finder.findLabel((WindowContext) mockWindowContext.proxy(), "label");
            fail("Should not have been able to find a label");
        } catch (Exception e) {
            //Expected
        }
    }

    private JMenuBar createMenuBar(String top) {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = createMenu(top);
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

    public void testFindsJAppletInSomeFrameGivenTheAppletName() {
        Frame frame1 = new Frame();
        Frame frame2 = new Frame();
        frame1.setVisible(true);
        frame2.setVisible(true);
        JApplet applet = new JApplet();
        applet.setName("testApplet");
        frame2.add(applet);
        assertEquals(applet, finder.findApplet("testApplet"));
        frame1.dispose();
        frame2.dispose();
    }
}

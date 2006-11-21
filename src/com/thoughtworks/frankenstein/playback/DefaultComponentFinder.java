package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.HierarchyEvent;
import java.util.logging.Logger;
import javax.swing.*;

import com.thoughtworks.frankenstein.naming.*;
import com.thoughtworks.frankenstein.common.RootPaneContainerFinder;

/**
 * Default component finder implementation
 *
 * @author Vivek Prahlad
 */
public class DefaultComponentFinder implements ComponentFinder {
    private Component editor;
    private NamingStrategy namingStrategy;
    private JPopupMenu popupMenu;

    public DefaultComponentFinder(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public Component findComponent(WindowContext parent, String componentName) {
        if (editor != null) {
            if (componentName.equals(editor.getName())) return editor;
        }
        Container activeWindow = (Container) parent.activeWindow();
        Component comp = findComp(componentName, activeWindow);
        if (comp == null) {
            namingStrategy.nameComponentsIn(activeWindow);
        }
        comp = findComp(componentName, activeWindow);
        if (comp == null) {
            throw new RuntimeException("Unable to find component with name: " + componentName + " in " + activeWindow.toString());
        }
        return comp;
    }

    public void setTableCellEditor(Component component) {
        this.editor = component;
    }

    private Component findComp(String componentName, Container parent) {
        ComponentNameMatchingRule rule = new ComponentNameMatchingRule(componentName);
        new ComponentHierarchyWalker().matchComponentsIn(parent, rule);
        return rule.matchingComponent();
    }

    public synchronized JMenuItem findMenuItem(WindowContext context, String path) {
        String[] pathElements = path.split(">");
        if (popupMenu != null) {
            return findPopupMenu(pathElements);
        } else {
            for (int i = 0; i < Frame.getFrames().length; i++) {
                try {
                    java.util.List menuBars = findMenuBar(Frame.getFrames(), i);
                    if (!menuBars.isEmpty()) {
                        return findMenuItem(menuBars, pathElements);
                    }
                } catch (Exception e) {
                    //Ignore
                }
            }
        }
        throw new RuntimeException("Unable to find menu with path: " + path);
    }

    private JMenuItem findMenuItem(java.util.List menuBars, String[] pathElements) {
        JMenuBar bar = (JMenuBar) menuBars.get(0);
        JMenuItem menuItem = findTopLevelMenu(bar, pathElements[0]);
        for (int j = 1; j < pathElements.length; j++) {
            menuItem = findMenu((JMenu) menuItem, pathElements[j]);
        }
        return menuItem;
    }

    private JMenuItem findPopupMenu(String[] pathElements) {
        JMenuItem menuItem = findTopLevelMenu(pathElements[0]);
        for (int j = 1; j < pathElements.length; j++) {
            menuItem = findMenu((JMenu) menuItem, pathElements[j]);
        }
        popupMenu = null;
        return menuItem;
    }

    private JMenuItem findTopLevelMenu(String pathElement) {
        for (int i=0; i<popupMenu.getComponentCount(); i++) {
            if (popupMenu.getComponent(i) instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) popupMenu.getComponent(i);
                if (pathElement.equals(item.getText())) return item;
            }
        }
        throw new RuntimeException("Unable to find top level popup menu item: " + pathElement);
    }

    private java.util.List findMenuBar(Frame[] frames, int i) {
        Frame frame = frames[i];
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JMenuBar.class);
        new ComponentHierarchyWalker().matchComponentsIn(frame, rule);
        return rule.unnamedComponents();
    }

    private JMenu findTopLevelMenu(JMenuBar bar, String pathElement) {
        for (int i = 0; i < bar.getMenuCount(); i++) {
            if (bar.getMenu(i).getText().equals(pathElement)) return bar.getMenu(i);
        }
        throw new RuntimeException("Unable to find top level menu" + pathElement);
    }

    private JMenuItem findMenu(JMenu menuItem, String pathElement) {
        for (int i = 0; i < menuItem.getMenuComponentCount(); i++) {
            if (menuItem.getMenuComponent(i) instanceof JMenuItem) {
                JMenuItem jmenuItem = (JMenuItem) menuItem.getMenuComponent(i);
                if (jmenuItem.getText().equals(pathElement)) return jmenuItem;
            }
        }
        throw new RuntimeException  ("Unable to find menu" + pathElement);
    }

    public Window findWindow(String title) {
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            if (title(frame).matches(title)) {
                return frame;
            }
        }
        throw new RuntimeException("Could not find window with title: " + title);
    }

    private String title(Frame frame) {
        return frame.getTitle() == null ? "" : frame.getTitle();
    }

    public JInternalFrame findInternalFrame(WindowContext windowContext, String title) {
        InternalFrameMatchingRule rule = new InternalFrameMatchingRule(title);
        new ComponentHierarchyWalker().matchComponentsIn((Container) windowContext.activeTopLevelWindow(), rule);
        return rule.matchingComponent();
    }

    public JFileChooser findFileChooser(WindowContext context) {
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JFileChooser.class);
        new ComponentHierarchyWalker().matchComponentsIn((Container) context.activeWindow(), rule);
        return (JFileChooser) rule.unnamedComponents().get(0);
    }

    public synchronized void menuDisplayed(JPopupMenu menu) {
        popupMenu = menu;
        Logger.getLogger("Frankenstein").info("Menu Displayed");
    }

    public void menuHidden() {
        popupMenu = null;
        Logger.getLogger("Frankenstein").info("Menu Hidden");
    }

}

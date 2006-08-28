package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.naming.*;

/**
 * Default component finder implementation
 * @author Vivek Prahlad
 */
public class DefaultComponentFinder implements ComponentFinder {
    private Component editor;
    private NamingStrategy namingStrategy;

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
            throw new RuntimeException("Unable to find component with name: " + componentName);
        }
        return comp;
    }

    public void setEditor(Component component) {
        this.editor = component;
    }

    private Component findComp(String componentName, Container parent) {
        ComponentNameMatchingRule rule = new ComponentNameMatchingRule(componentName);
        new ComponentHierarchyWalker().matchComponentsIn(parent, rule);
        return rule.matchingComponent();
    }

    public JMenuItem findMenuItem(WindowContext context, String path) {
        String[] pathElements = path.split(">");
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            java.util.List menuBars = findMenuBar(frames, i);
            if (!menuBars.isEmpty()) {
                JMenuBar bar = (JMenuBar) menuBars.get(0);
                JMenuItem menuItem = findTopLevelMenu(bar, pathElements[0]);
                for (int j = 1; j < pathElements.length; j++) {
                    menuItem = findMenu((JMenu) menuItem, pathElements[j]);
                }
                return menuItem;
            }
        }
        throw new RuntimeException("Unable to find menu with path: " + path);
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
            JMenuItem jmenuItem = (JMenuItem) menuItem.getMenuComponent(i);
            if (jmenuItem.getText().equals(pathElement)) return jmenuItem;
        }
        throw new RuntimeException("Unable to find menu" + pathElement);
    }

    public Window findWindow(String title) {
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            if (title.equals(frame.getTitle())) {
                return frame;
            }
        }
        throw new RuntimeException("Could not find window with title: " + title);
    }

    public JInternalFrame findInternalFrame(WindowContext windowContext, String title) {
            InternalFrameMatchingRule rule = new InternalFrameMatchingRule(title);
            new ComponentHierarchyWalker().matchComponentsIn((Container) windowContext.activeWindow(), rule);
            return rule.matchingComponent();
    }

    public JFileChooser fileChooser(WindowContext context) {
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JFileChooser.class);
        new ComponentHierarchyWalker().matchComponentsIn((Container) context.activeWindow(), rule);
        return (JFileChooser) rule.unnamedComponents().get(0);
    }
}

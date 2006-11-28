package com.thoughtworks.frankenstein.playback;

import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;

import com.thoughtworks.frankenstein.naming.*;

/**
 * Default component finder implementation
 *
 * @author Vivek Prahlad
 */
public class DefaultComponentFinder implements ComponentFinder {
    private Component editor;
    private NamingStrategy namingStrategy;
    private DialogList dialogList = new DialogList();
    private MenuFinder menuFinder = new MenuFinder();

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
        return menuFinder.find(path);
    }

    public synchronized void menuDisplayed(JPopupMenu menu) {
        menuFinder.menuDisplayed(menu);
    }

    public void menuHidden() {
        menuFinder.menuHidden();
    }

    public Window findWindow(String title) {
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            if (MatchStrategy.matchValues(title(frames[i]),title)) {
                return frames[i];
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

    public JLabel findLabel(WindowContext context,String labelValue) {
        LabelMatchingRule rule = new LabelMatchingRule(labelValue);
        new ComponentHierarchyWalker().matchComponentsIn((Container) context.activeWindow(), rule);
        if (rule.matchingLabel() == null) throw new RuntimeException("Unable to find label: " + labelValue);
        return rule.matchingLabel();
    }

    public JDialog findDialog(String title) {
        return dialogList.findDialog(title);
    }
}

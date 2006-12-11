package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import com.thoughtworks.frankenstein.events.SelectTreeEvent;
import com.thoughtworks.frankenstein.events.TreeEvent;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording tree selection events.
 * @author Vivek Prahlad
 */
public class SelectTreeRecorder extends AbstractComponentRecorder implements TreeSelectionListener {
    private MouseListener mouseListener = new MouseListener();

    public SelectTreeRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JTree.class);
    }

    void componentShown(Component component) {
        tree(component).addTreeSelectionListener(this);
        tree(component).addMouseListener(mouseListener);
    }

    private JTree tree(Component component) {
        return (JTree) component;
    }

    void componentHidden(Component component) {
        tree(component).removeTreeSelectionListener(this);
        tree(component).removeMouseListener(mouseListener);
    }

    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        if (path == null) return;
        recorder.record(new SelectTreeEvent(componentName((Component) e.getSource()), treePath(path)));
    }

    private String treePath(TreePath path) {
        String treePath = "";
        for (int i = 0; i < path.getPathCount(); i++) {
            Object pathComponent = path.getPathComponent(i);
            if (i > 0) {
                treePath += ">";
            }
            treePath += pathComponent.toString();
        }
        return treePath;
    }

    private void recordRightClick(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTree tree = (JTree) e.getSource();
            recorder.record(new TreeEvent(componentName(tree), treePath(tree.getSelectionPath()), new RightClickAction()));
        }
    }

    private class MouseListener extends MouseAdapter {
        public void mouseReleased(MouseEvent e) {
            recordRightClick(e);
        }
    }
}

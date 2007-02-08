package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.thoughtworks.frankenstein.events.SelectTreeEvent;
import com.thoughtworks.frankenstein.events.TreeEvent;
import com.thoughtworks.frankenstein.events.actions.Action;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording tree selection events.
 *
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

    private String[] treePath(TreePath path) {
        String[] treePath = new String[path.getPathCount()];
        for (int i = 0; i < path.getPathCount(); i++) {
            Object pathComponent = path.getPathComponent(i);
            treePath[i] = pathComponent.toString();
        }
        return treePath;
    }

    private void recordRightClick(MouseEvent e) {
        if (e.isPopupTrigger()) {
            recordTreeEvent(e, new RightClickAction());
        }
    }

    private void recordDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            recordTreeEvent(e, new DoubleClickAction());
        }
    }

    protected void recordTreeEvent(MouseEvent e, Action action) {
        JTree tree = (JTree) e.getSource();
        TreePath path = tree.getLeadSelectionPath();
        if (path != null) {
            recorder.record(new TreeEvent(componentName(tree), treePath(path), action));
        }
    }

    private class MouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            recordDoubleClick(e);
        }

        public void mousePressed(MouseEvent e) {
            recordRightClick(e);
        }

        public void mouseReleased(MouseEvent e) {
            recordRightClick(e);
        }
    }
}

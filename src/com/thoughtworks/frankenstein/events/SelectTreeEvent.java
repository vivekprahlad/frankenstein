package com.thoughtworks.frankenstein.events;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands tree selection
 * @author Vivek Prahlad
 */
public class SelectTreeEvent extends AbstractFrankensteinEvent {
    private String treeName;
    private String path;

    public SelectTreeEvent(String treeName, String path) {
        this.treeName = treeName;
        this.path = path;
    }

    public SelectTreeEvent(String scriptLine) {
        this(params(scriptLine)[0], params(scriptLine)[1]);
    }

    public String toString() {
        return "SelectTreeEvent: Tree: " + treeName + ", Path: " + path;
    }

    private void select(JTree tree) {
        String[] nodeNames = path.split(">");
        TreePath path = findRoot(tree, nodeNames[0]);
        for (int i = 1; i < nodeNames.length; i++) {
            path = findChild(tree, path, nodeNames[i]);
        }
        tree.setLeadSelectionPath(path);
        tree.setSelectionPath(path);
    }

    private TreePath findRoot(JTree tree, String rootPath) {
        Object root = tree.getModel().getRoot();
        if (root.toString().equalsIgnoreCase(rootPath)) {
            return new TreePath(root);
        }
        throw new RuntimeException("Root does not exist. Test specified " + rootPath + " but was " + root.toString());
    }

    private TreePath findChild(JTree tree, TreePath currentPath, String path) {
        Object lastPathComponent = currentPath.getLastPathComponent();
        TreeModel model = tree.getModel();
        int childCount = model.getChildCount(lastPathComponent);
        for(int j=0; j < childCount;j++){
            Object child = model.getChild(lastPathComponent,j);
            if(child.toString().equalsIgnoreCase(path)) {
                TreePath newPath = currentPath.pathByAddingChild(child);
                tree.expandPath(newPath);
                return newPath;
            }
        }
        throw new RuntimeException("Root does not exist. Test specified " + path + " but did not exist.");
    }

    public String target() {
        return treeName;
    }

    public String parameters() {
        return path;
    }

    public void run() {
        JTree tree = (JTree) finder.findComponent(context, treeName);
        select(tree);
    }
}

package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;

import com.thoughtworks.frankenstein.playback.MatchStrategy;

/**
 * Understands tree selection
 * @author Vivek Prahlad
 */
public class SelectTreeEvent extends AbstractFrankensteinEvent {
    private String treeName;
    private String[] path;

    public SelectTreeEvent(String treeName, String[] path) {
        this.treeName = treeName;
        this.path = path;
    }

    public SelectTreeEvent(String scriptLine) {
        this(Tree.name(scriptLine), Tree.path(scriptLine));
    }

    public String toString() {
        return "SelectTreeEvent: Tree: " + treeName + ", Path: " + Tree.pathString(path, ">");
    }

    public String scriptLine() {
        return (underscore(action()) + " \"" + target() + "\"," + Tree.pathString(path, ",", "\""));
    }

    private void select(JTree tree) {
        String[] nodeNames = path;
        TreePath path = findRoot(tree, nodeNames[0]);
        for (int i = 1; i < nodeNames.length; i++) {
            path = findChild(tree, path, nodeNames[i]);
        }
        tree.setLeadSelectionPath(path);
        tree.setSelectionPath(path);
    }

    private TreePath findRoot(JTree tree, String rootPath) {
        Object root = tree.getModel().getRoot();
        if (MatchStrategy.matchValues(root.toString(),rootPath)) {
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
            if(MatchStrategy.matchValues(child.toString(),path)) {
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
        return Tree.pathString(path, ">");
    }

    public void run() {
        JTree tree = (JTree) finder.findComponent(context, treeName);
        select(tree);
    }
}

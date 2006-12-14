package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.events.actions.Action;

import javax.swing.*;
import java.awt.*;

/**
 * Understands actions on trees
 *
 * @author Vivek Prahlad
 */
public class TreeEvent extends AbstractCompoundEvent {
    private String treeName;
    private String path;

    public TreeEvent(String treeName, String path, Action action) {
        super(action);
        this.treeName = treeName;
        this.path = path;
    }

    public TreeEvent(String scriptLine, Action action) {
        this(params(scriptLine)[0], params(scriptLine)[1], action);
    }

    public String toString() {
        return action.name() + "TreeEvent: Tree: " + treeName + ", Path: " + path;
    }

    public String target() {
        return treeName;
    }

    public String parameters() {
        return path;
    }

    public void run() {
        JTree tree = (JTree) finder.findComponent(context, treeName);
        action.execute(nodeLocation(tree), tree, finder, context);
    }

    private Point nodeLocation(JTree tree) {
        Point treeLocation = tree.getLocation();
        Rectangle pathBounds = tree.getPathBounds(tree.getSelectionPath());
        Point nodeLocation = new Point(pathBounds.x + pathBounds.width/2, pathBounds.y + pathBounds.height/2);
        treeLocation.translate(nodeLocation.x, nodeLocation.y);
        return treeLocation;
    }
}

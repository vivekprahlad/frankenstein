package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.HierarchyEvent;

/**
 * Understands right clicking on trees
 *
 * @author Vivek Prahlad
 */
public class RightClickTreeEvent extends AbstractFrankensteinEvent implements AWTEventListener {
    private String treeName;
    private String path;

    public RightClickTreeEvent(String treeName, String path) {
        this.treeName = treeName;
        this.path = path;
        this.eventExecutionStrategy = EventExecutionStrategy.IN_PLAYER_THREAD;
    }

    public RightClickTreeEvent(String scriptLine) {
        this(params(scriptLine)[0], params(scriptLine)[1]);
    }

    public String toString() {
        return "RightClickTreeEvent: Tree: " + treeName + ", Path: " + path;
    }

    public String target() {
        return treeName;
    }

    public String parameters() {
        return path;
    }

    public synchronized void run() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.HIERARCHY_EVENT_MASK);
        JTree tree = (JTree) finder.findComponent(context, treeName);
        new SelectTreeEvent(treeName, path).play(context, finder, scriptContext, robot);
        rightClick(nodeLocation(tree), tree);
        try {
            wait(10000);
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void rightClick(Point point, JComponent tree) {
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(tree, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, point.x, point.y, 0, true));
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(new MouseEvent(tree, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, point.x, point.y, 0, false));
    }

    private Point nodeLocation(JTree tree) {
        Point treeLocation = tree.getLocationOnScreen();
        Rectangle pathBounds = tree.getPathBounds(tree.getSelectionPath());
        Point nodeLocation = new Point(pathBounds.x + pathBounds.width/2, pathBounds.y +pathBounds.height/2);
        nodeLocation.translate(treeLocation.x, treeLocation.y);
        return nodeLocation;
    }

    public synchronized void eventDispatched(AWTEvent event) {
        if (event instanceof HierarchyEvent) {
            HierarchyEvent he = (HierarchyEvent) event;
            if (isComponentDisplayableEvent(he) && matchesComponentType(event)) {
                Component component = (Component) event.getSource();
                if (component.isDisplayable()) {
                    finder.menuDisplayed((JPopupMenu) component);
                } else {
                    finder.menuHidden();
                }
                notifyAll();
            }
        }
    }

    private boolean matchesComponentType(AWTEvent event) {
        return event.getSource() instanceof JPopupMenu;
    }

    private boolean isComponentDisplayableEvent(HierarchyEvent he) {
        return (he.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0;
    }
}

package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.events.actions.*;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jmock.Mock;

import java.awt.*;

/**
 * Ensures behaviour of TreeEvent
 * @author Vivek Prahlad
 */
public class TreeEventTest extends AbstractEventTestCase {
    private DefaultMutableTreeNode one;
    private DefaultMutableTreeNode two;
    private DefaultMutableTreeNode three;

    public void testEqualsAndHashCode() {
        TreeEvent one = new TreeEvent("tree", "one>two>three", new RightClickAction());
        TreeEvent two = new TreeEvent("tree", "one>two>three", new RightClickAction());
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("RightClickTreeEvent: Tree: tree, Path: one>two>three", new TreeEvent("tree", "one>two>three", new RightClickAction()).toString());
    }

    public void testAction() {
        assertEquals("RightClickTree", new TreeEvent("tree", "one>two>three", new RightClickAction()).action());
    }

    public void testTarget() {
        assertEquals("tree", new TreeEvent("tree", "one>two>three", new RightClickAction()).target());
    }

    public void testParameters() {
        assertEquals("one>two>three", new TreeEvent("tree", "one>two>three", new RightClickAction()).parameters());
    }

    public void testScriptLine() {
        assertEquals("right_click_tree \"tree\" , \"one>two>three\"", new TreeEvent("tree", "one>two>three", new RightClickAction()).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        Mock mockFinder = mock(ComponentFinder.class);
        JTree tree = tree();
        tree.setSelectionPath(new TreePath(new Object[]{one, two, three}));
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("tree")).will(returnValue(tree));
        Mock mockAction = mock(com.thoughtworks.frankenstein.events.actions.Action.class);
        mockAction.expects(once()).method("execute").with(eq(new Point(66,46)), ANYTHING, ANYTHING, ANYTHING);
        TreePath path = tree.getPathForLocation(66, 46);
        assertSame(one, path.getPath()[0]);
        assertSame(two, path.getPath()[1]);
        assertSame(three, path.getPath()[2]);
        new TreeEvent("tree", "one>two>three", (com.thoughtworks.frankenstein.events.actions.Action) mockAction.proxy()).play(null, (ComponentFinder) mockFinder.proxy(), null, null);
    }

    private JTree tree() {
        one = new DefaultMutableTreeNode("one", true);
        two = new DefaultMutableTreeNode("two", true);
        three = new DefaultMutableTreeNode("three", false);
        one.add(two);
        two.add(three);
        JTree tree = new JTree(one);
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(tree));
        frame.pack();
        return tree;
    }

    protected FrankensteinEvent createEvent() {
        return new TreeEvent("tree", "one>two>three", new RightClickAction());
    }
}

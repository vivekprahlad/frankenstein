package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.events.actions.RightClickAction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Ensures behaviour of TreeEvent
 * @author Vivek Prahlad
 */
public class TreeEventTest extends AbstractEventTestCase {

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
//        Mock mockFinder = mock(ComponentFinder.class);
//        JTree tree = tree();
//        tree.setSelectionRow(0);
//        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("tree")).will(returnValue(tree));
//        new TreeEvent("tree", "one>two>three").play(null, (ComponentFinder) mockFinder.proxy(), null, null);
    }

    private JTree tree() {
        DefaultMutableTreeNode one = new DefaultMutableTreeNode("one", true);
        DefaultMutableTreeNode two = new DefaultMutableTreeNode("two", true);
        DefaultMutableTreeNode three = new DefaultMutableTreeNode("three", false);
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

package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Ensures behaviour of SelectTreeEvent
 */
public class SelectTreeEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        SelectTreeEvent one = new SelectTreeEvent("name", new String[] {"one", "two", "three"});
        SelectTreeEvent two = new SelectTreeEvent("name", new String[] {"one", "two", "three"});
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("SelectTreeEvent: Tree: name, Path: one>two>three", new SelectTreeEvent("name", new String[] {"one", "two", "three"}).toString());
    }

    public void testAction() {
        assertEquals("SelectTree", new SelectTreeEvent("name", new String[] {"one", "two", "three"}).action());
    }

    public void testTarget() {
        assertEquals("name", new SelectTreeEvent("name", new String[] {"one", "two", "three"}).target());
    }

    public void testParameters() {
        assertEquals("one>two>three", new SelectTreeEvent("name", new String[] {"one", "two", "three"}).parameters());
    }

    public void testScriptLine() {
        assertEquals("select_tree \"name\",\"one\",\"two\",\"three\"", new SelectTreeEvent("name", new String[] {"one", "two", "three"}).scriptLine());
    }

    public void testPlaysEvent() {
        Mock componentFinder = mock(ComponentFinder.class);
        JTree tree = tree();
        componentFinder.expects(once()).method("findComponent").will(returnValue(tree));
        new SelectTreeEvent("name", new String[] {"one", "two", "three"}).play(null, (ComponentFinder) componentFinder.proxy(), null, null);
        TreePath selectionPath = tree.getSelectionPath();
        assertEquals(node(selectionPath, 0).getUserObject(), "one");
        assertEquals(node(selectionPath, 1).getUserObject(), "two");
        assertEquals(node(selectionPath, 2).getUserObject(), "three");
    }

    public void testPlaysEventWithRegularExpression() {
        Mock componentFinder = mock(ComponentFinder.class);
        JTree tree = tree();
        componentFinder.expects(once()).method("findComponent").will(returnValue(tree));
        new SelectTreeEvent("name", new String[] {"regex:o.*", "two", "regex:th.*"}).play(null, (ComponentFinder) componentFinder.proxy(), null, null);
        TreePath selectionPath = tree.getSelectionPath();
        assertEquals(node(selectionPath, 0).getUserObject(), "one");
        assertEquals(node(selectionPath, 1).getUserObject(), "two");
        assertEquals(node(selectionPath, 2).getUserObject(), "three");
    }

    public void testPlayWithNonExistentRootNode() {
        Mock componentFinder = mock(ComponentFinder.class);
        JTree tree = tree();
        componentFinder.expects(once()).method("findComponent").will(returnValue(tree));
        try {
            new SelectTreeEvent("name", new String[] {"notthere", "two", "three"}).play(null, (ComponentFinder) componentFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }

    public void testPlayWithNonExistentChildNode() {
        Mock componentFinder = mock(ComponentFinder.class);
        JTree tree = tree();
        componentFinder.expects(once()).method("findComponent").will(returnValue(tree));
        try {
            new SelectTreeEvent("name", new String[] {"one", "notthere", "three"}).play(null, (ComponentFinder) componentFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }

    private DefaultMutableTreeNode node(TreePath selectionPath, int index) {
        return (DefaultMutableTreeNode) selectionPath.getPathComponent(index);
    }

    private JTree tree() {
        DefaultMutableTreeNode one = new DefaultMutableTreeNode("one", true);
        DefaultMutableTreeNode two = new DefaultMutableTreeNode("two", true);
        DefaultMutableTreeNode three = new DefaultMutableTreeNode("three", false);
        one.add(two);
        two.add(three);
        return new JTree(one);
    }

    protected FrankensteinEvent createEvent() {
        return new SelectTreeEvent("name", new String[] {"one", "two", "three"});
    }
}

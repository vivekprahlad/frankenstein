package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.events.SelectTreeEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the switch recorder.
 */
public class SelectTreeRecorderTest extends AbstractRecorderTestCase {
    private JTree tree;
    private TreePath path;
    private SelectTreeRecorder recorder;
    private Mock mockVisibility;

    protected void setUp() throws Exception {
        super.setUp();
        DefaultMutableTreeNode one = new DefaultMutableTreeNode("one", true);
        DefaultMutableTreeNode two = new DefaultMutableTreeNode("two", true);
        DefaultMutableTreeNode three = new DefaultMutableTreeNode("three", false);
        path = new TreePath(new Object[]{one, two, three});
        one.add(two);
        two.add(three);
        tree = new JTree(one);
        tree.setName("testTree");
        mockVisibility = mock(ComponentVisibility.class);
        recorder = new SelectTreeRecorder((EventRecorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsListenerToTreeWhenIsShown() {
        int listenerCount = treeListenerCount();
        recorder.componentShown(tree);
        assertTrue(treeListenerCount() == listenerCount + 1);
    }

    private int treeListenerCount() {
        return tree.getTreeSelectionListeners().length;
    }

    public void testRemovesListenerWhenTabbedPaneIsHidden() {
        int listenerCount = treeListenerCount();
        recorder.componentShown(tree);
        recorder.componentHidden(tree);
        assertEquals(listenerCount,  treeListenerCount());
    }

    public void testSelectingTreePostsSelectEvent() {
        recorder.componentShown(tree);
        mockRecorder.expects(once()).method("record").with(eq(new SelectTreeEvent("testTree", "one>two>three")));
        tree.setSelectionPath(path);
    }

    public void testDoesNotRecordTreeSelectionEventIfPathIsNull() {
        recorder.componentShown(tree);
        mockRecorder.expects(never()).method("record").with(ANYTHING);
        tree.setSelectionPath(null);
    }
}

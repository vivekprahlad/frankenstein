package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Ensures behaviour of TreeEvent
 *
 * @author Vivek Prahlad
 */
public class TreeEventTest extends AbstractEventTestCase {
    private DefaultMutableTreeNode one;
    private DefaultMutableTreeNode two;
    private DefaultMutableTreeNode three;
    private TreeEventTest.MockMouseListener mockMouseListener;

    protected void setUp() throws Exception {
        super.setUp();
        mockMouseListener = new MockMouseListener();
    }

    public void testEqualsAndHashCode() {
        TreeEvent one = new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction());
        TreeEvent two = new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction());
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("RightClickTreeEvent: Tree: tree, Path: one>two>three", new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction()).toString());
    }

    public void testAction() {
        assertEquals("RightClickTree", new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction()).action());
    }

    public void testTarget() {
        assertEquals("tree", new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction()).target());
    }

    public void testParameters() {
        assertEquals("one>two>three", new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction()).parameters());
    }

    public void testScriptLine() {
        assertEquals("right_click_tree \"tree\",\"one\",\"two\",\"three\"", new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction()).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        Mock mockFinder = mock(ComponentFinder.class);
        JTree tree = tree();
        tree.setSelectionPath(new TreePath(new Object[]{one, two, three}));
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("tree")).will(returnValue(tree));
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("tree")).will(returnValue(tree));
        tree.addMouseListener(mockMouseListener);
        new TreeEvent("tree", new String[]{"one", "two", "three"}, new DoubleClickAction()).play(new DefaultWindowContext(), (ComponentFinder) mockFinder.proxy(), null, null);
        waitForIdle();
        TreePath path = tree.getPathForLocation(mockMouseListener.point.x, mockMouseListener.point.y);
        assertSame(one, path.getPath()[0]);
        assertSame(two, path.getPath()[1]);
        assertSame(three, path.getPath()[2]);
    }

    public void testReplacesSelectTreeEvent() {
        SelectTreeEvent treeEvent = new SelectTreeEvent("tree", new String[]{"one", "two", "three"});
        TreeEvent event = new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction());
        Mock mockEventList = mock(EventList.class);
        mockEventList.expects(once()).method("replaceLastEvent").with(same(event));
        event.record((EventList) mockEventList.proxy(), treeEvent);
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
        return new TreeEvent("tree", new String[]{"one", "two", "three"}, new RightClickAction());
    }

    private class MockMouseListener extends MouseAdapter {
        private Point point;

        public void mouseClicked(MouseEvent e) {
            point = e.getPoint();
        }
    }
}

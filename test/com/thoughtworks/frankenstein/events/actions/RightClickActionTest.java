package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.common.WaitForIdle;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;

/**
 * Ensures that RightClickAction behaves properly
 */
public class RightClickActionTest extends MockObjectTestCase {
    private MockMouseListener mockMouseListener;
    private RightClickAction rightClickAction;
    private JTable table;
    private JFrame frame;

    protected void setUp() throws Exception {
        super.setUp();
        mockMouseListener = new MockMouseListener();
        rightClickAction = new RightClickAction();
        table = new JTable(3, 3);
        frame = new JFrame();
        frame.getContentPane().add(table);
        frame.pack();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        frame.dispose();
    }

    public void testName() {
        assertEquals("RightClick", new RightClickAction().name());
    }

    public void testClicksOnComponent() {
        table.addMouseListener(mockMouseListener);
        rightClickAction.execute(new Point(table.getWidth() / 2, table.getRowHeight() / 2), table, null, new DefaultWindowContext());
        waitForIdle();
        assertTrue(mockMouseListener.clicked);
        assertEquals(0, table.rowAtPoint(mockMouseListener.point));
    }

    private void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

    private class MockMouseListener extends MouseAdapter {
        private boolean clicked;
        private Point point;

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                clicked = true;
                point = e.getPoint();
            }
        }
    }
}
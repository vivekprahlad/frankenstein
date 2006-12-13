package com.thoughtworks.frankenstein.events.actions;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;


import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.WaitForIdle;
import com.thoughtworks.frankenstein.application.ThreadUtil;

import javax.swing.*;

/**
 * Ensures that RightClickAction behaves properly
 */
public class RightClickActionTest extends MockObjectTestCase {
    private MockMouseListener mockMouseListener;
    private Mock mockWindowContext;
    private RightClickAction rightClickAction;
    private JTable table;
    private JFrame frame;
    private static final Object LOCK = new Object();

    protected void setUp() throws Exception {
        super.setUp();
        mockMouseListener = new MockMouseListener();
        mockWindowContext = mock(WindowContext.class);
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
        assertEquals(225, table.getWidth());
        assertEquals(48, table.getHeight());
        rightClickAction.execute(new Point(110, 24), table, null, (WindowContext) mockWindowContext.proxy());
        waitForIdle();
        assertTrue(mockMouseListener.clicked);
        assertEquals(new Point(110,24), mockMouseListener.point);
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
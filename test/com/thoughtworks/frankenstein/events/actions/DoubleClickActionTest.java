package com.thoughtworks.frankenstein.events.actions;

import com.thoughtworks.frankenstein.playback.WindowContext;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 *
 */
public class DoubleClickActionTest extends MouseActionTestCase {
    private MockMouseListener mockMouseListener;
    private DoubleClickAction doubleClickAction;

    public void testClicksOnComponent() {
        mockMouseListener=new MockMouseListener();
        doubleClickAction=new DoubleClickAction();
        button.addMouseListener(mockMouseListener);
        expectAddAndRemoveWindowContextListener(doubleClickAction);
        doubleClickAction.execute(center(button), button, null, (WindowContext) mockWindowContext.proxy());
        waitForIdle();
        assertTrue(mockMouseListener.doubleClicked);
    }

    public void testName() {
        assertEquals("DoubleClick", new DoubleClickAction().name());
    }

    private class MockMouseListener extends MouseAdapter {
        private boolean doubleClicked;

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                doubleClicked = true;
            }
        }
    }
}

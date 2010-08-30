package com.thoughtworks.frankenstein.events.actions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Ensures the behaviour of DropAction.
 *
 * @author Pavan.
 */
public class DropActionTest extends MouseActionTestCase {
    private DropAction dropAction;
    private MockMouseListener mockMouseListener;

    protected void setUp() throws Exception {
        super.setUp();
        mockMouseListener = new MockMouseListener();
        dropAction = new DropAction();
    }

    public void testName() {
        assertEquals("Drop", new DropAction().name());
    }

    public void testDropingOnAComponent() {
        button.addMouseListener(mockMouseListener);
        expectAddAndRemoveWindowContextListener(dropAction);
        dropAction.execute(center(button), button, null, (WindowContext) mockWindowContext.proxy());
        waitForIdle();
        assertTrue(mockMouseListener.mouseReleased);
    }

    private class MockMouseListener extends MouseAdapter {
        private boolean mouseReleased;

        public void mouseReleased(MouseEvent e) {
            mouseReleased = true;
        }
    }
}

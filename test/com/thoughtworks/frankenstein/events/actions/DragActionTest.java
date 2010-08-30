package com.thoughtworks.frankenstein.events.actions;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Ensures the behaviour of the DragAction
 *
 * @author Pavan
 */
public class DragActionTest extends MouseActionTestCase {
    private MockMouseInputAdapter mockMouseInputAdapter;
    private DragAction dragAction;

    protected void setUp() throws Exception {
        super.setUp();
        mockMouseInputAdapter = new MockMouseInputAdapter();
        dragAction = new DragAction();
    }

    public void testName() {
        assertEquals("Drag", new DragAction().name());
    }

    public void testDragAComponent() {
        button.addMouseListener(mockMouseInputAdapter);
        button.addMouseMotionListener(mockMouseInputAdapter);
        expectAddAndRemoveWindowContextListener(dragAction);
        dragAction.execute(center(button), button, null, (WindowContext) mockWindowContext.proxy());
        waitForIdle();
        assertTrue(mockMouseInputAdapter.mousePressed);
        assertTrue(mockMouseInputAdapter.mouseDragged);
        assertTrue(mockMouseInputAdapter.mouseExited);
    }

    private class MockMouseInputAdapter extends MouseInputAdapter {

        private boolean mouseDragged = false;
        private boolean mousePressed = false;
        private boolean mouseExited = false;

        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1){
                mousePressed = true;
            }
        }

        public void mouseDragged(MouseEvent e) {
            mouseDragged = true;
        }

        public void mouseExited(MouseEvent e) {
            mouseExited = true;
        }
    }
}

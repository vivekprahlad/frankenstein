package com.thoughtworks.frankenstein.events.actions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thoughtworks.frankenstein.playback.WindowContext;


/**
 * Ensures behaviour of DoubleClickAction
 */
public class DoubleClickActionTest extends MouseActionTestCase {
    private MockMouseListener mockMouseListener;
    private DoubleClickAction doubleClickAction;

    protected void setUp() throws Exception {
        super.setUp();
        doubleClickAction = new DoubleClickAction();
    }

    public void testClicksOnComponent() {
        mockMouseListener = new MockMouseListener();
        button.addMouseListener(mockMouseListener);
        expectAddAndRemoveWindowContextListener(doubleClickAction);
        doubleClickAction.execute(center(button), button, null, (WindowContext) mockWindowContext.proxy());
        waitForIdle();
        assertTrue(mockMouseListener.doubleClicked);
    }

    public void testDoesNotNotifyWhenSingleClickOccurs() {
        final Notifier notifier = new Notifier(doubleClickAction);
        Thread notifierThread = new Thread(new Runnable() {
            public void run() {
                notifier.waitForMouseRelease();
            }
        });
        notifierThread.start();
        while(!notifier.waiting); //Wait for the thread to start.
        assertFalse(notifier.notified);
        doubleClickAction.mouseReleased(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 1,false));
        assertTrue(notifierThread.isAlive());
        assertFalse(notifier.notified);
    }

    public void testNotifiesWhenDoubleClickOccurs() throws InterruptedException {
        final Notifier notifier = new Notifier(doubleClickAction);
        Thread notifierThread = new Thread(new Runnable() {
            public void run() {
                notifier.waitForMouseRelease();
            }
        });
        notifierThread.start();
        while(!notifier.waiting); //Wait for the thread to start.
        assertFalse(notifier.notified);
        doubleClickAction.mouseReleased(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 2,false));
        notifierThread.join();
        assertTrue(notifier.notified);
    }

    public void testName() {
        assertEquals("DoubleClick", doubleClickAction.name());
    }

    private class Notifier {
        private DoubleClickAction doubleClickAction;
        private boolean notified = false;
        private boolean waiting = false;

        public Notifier(DoubleClickAction doubleClickAction) {
            this.doubleClickAction = doubleClickAction;
        }

        private void waitForMouseRelease() {
            synchronized(doubleClickAction) {
                try {
                    waiting = true;
                    doubleClickAction.wait();
                    notified = true;
                } catch (InterruptedException e) {
              }
            }
        }
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

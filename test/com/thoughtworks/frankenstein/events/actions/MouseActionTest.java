package com.thoughtworks.frankenstein.events.actions;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.thoughtworks.frankenstein.application.ThreadUtil;
import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 *
 */
public class MouseActionTest extends MouseActionTestCase {
    private MockActionListener mockActionListener;
    private ClickAction clickAction;
    private static final Object LOCK = new Object();

    protected void setUp() throws Exception {
        super.setUp();
        mockActionListener = new MockActionListener();
        clickAction = new ClickAction();
    }

    public void testWaitsForDialogToShow() throws InterruptedException {
        button.addActionListener(mockActionListener);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    synchronized (LOCK) {
                        LOCK.wait();//Simulate a dialog appearing.
                    }
                } catch (InterruptedException e1) {
                }
            }
        });
        expectAddAndRemoveWindowContextListener(clickAction);
        Thread dialogNotifier = new Thread(new Runnable() {
            public void run() {
                ThreadUtil.sleep(200);
                clickAction.dialogShown();
            }
        });
        dialogNotifier.start();
        clickAction.execute(new Point(button.getWidth() / 2, button.getHeight() / 2), button, null, (WindowContext) mockWindowContext.proxy());
        assertFalse(mockActionListener.clicked);
        synchronized (LOCK) {
            LOCK.notifyAll();
        }
    }

    private class MockActionListener implements ActionListener {
        private boolean clicked;

        public void actionPerformed(ActionEvent e) {
            clicked = true;
        }
    }

}

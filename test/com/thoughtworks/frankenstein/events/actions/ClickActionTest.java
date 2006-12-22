package com.thoughtworks.frankenstein.events.actions;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Ensures behaviour of ClickAction
 * @author vivek
 */
public class ClickActionTest extends MouseActionTestCase {
    private ClickAction clickAction;

    public void testClicksOnComponent() {
        MockActionListener mockActionListener=new MockActionListener();
        clickAction=new ClickAction();
        button.addActionListener(mockActionListener);
        expectAddAndRemoveWindowContextListener(clickAction);
        clickAction.execute(center(button), button, null, (WindowContext) mockWindowContext.proxy());
        waitForIdle();
        assertTrue(mockActionListener.clicked);
    }

    public void testName() {
        assertEquals("Click", new ClickAction().name());
    }

    private class MockActionListener implements ActionListener {
        private boolean clicked;
        public void actionPerformed(ActionEvent e) {
            clicked = true;
        }
    }

}

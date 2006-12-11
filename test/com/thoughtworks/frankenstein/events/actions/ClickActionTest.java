package com.thoughtworks.frankenstein.events.actions;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.WaitForIdle;
import com.thoughtworks.frankenstein.application.ThreadUtil;

/**
 * Ensures behaviour of ClickAction
 * @author vivek
 */
public class ClickActionTest extends MockObjectTestCase {
    private MockActionListener mockActionListener;
    private Mock mockWindowContext;
    private ClickAction clickAction;
    private JButton button;
    private JFrame frame;
    private static final Object LOCK = new Object();

    protected void setUp() throws Exception {
        super.setUp();
        mockActionListener = new MockActionListener();
        mockWindowContext = mock(WindowContext.class);
        clickAction = new ClickAction();
        button = new JButton();
        frame = new JFrame();
        frame.getContentPane().add(button);
        frame.pack();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        frame.dispose();
    }

    public void testClicksOnComponent() {
        button.addActionListener(mockActionListener);
        expectAddAndRemoveWindowContextListener();
        assertEquals(34, button.getWidth());
        assertEquals(10, button.getHeight());
        clickAction.execute(new Point(17,5), button, null, (WindowContext) mockWindowContext.proxy());
        waitForIdle();
        assertTrue(mockActionListener.clicked);
    }

    public void testName() {
        assertEquals("Click", new ClickAction().name());
    }

    public void testWaitsForDialogToShow() throws InterruptedException {
        button.addActionListener(mockActionListener);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    synchronized(LOCK) {
                        LOCK.wait();//Simulate a dialog appearing.
                    }
                } catch (InterruptedException e1) {
                }
            }
        });
        expectAddAndRemoveWindowContextListener();
        Thread dialogNotifier = new Thread(new Runnable() {
            public void run() {
                    ThreadUtil.sleep(200);
                    clickAction.dialogShown();
            }
        });
        dialogNotifier.start();
        clickAction.execute(new Point(button.getWidth()/2,button.getHeight()/2), button, null, (WindowContext) mockWindowContext.proxy());
        assertFalse(mockActionListener.clicked);
        synchronized(LOCK) {
            LOCK.notifyAll();
        }
    }

    private void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

    private void expectAddAndRemoveWindowContextListener() {
        mockWindowContext.expects(once()).method("addWindowContextListener").with(same(clickAction));
        mockWindowContext.expects(once()).method("removeWindowContextListener").with(same(clickAction));
    }

    private class MockActionListener implements ActionListener {
        private boolean clicked;
        public void actionPerformed(ActionEvent e) {
            clicked = true;
        }
    }
}

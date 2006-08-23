package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.common.WaitForIdle;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Ensures behaviour of windowactivated event.
 */
public class ActivateWindowEventTest extends MockObjectTestCase {
    public static final Object FOCUS_LOCK = new Object();

    public void testEqualsAndHashCode() {
        ActivateWindowEvent one = new ActivateWindowEvent("title");
        ActivateWindowEvent two = new ActivateWindowEvent("title");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("ActivateWindowEvent: title", new ActivateWindowEvent("title").toString());
    }

    public void testAction() {
        assertEquals("ActivateWindow", new ActivateWindowEvent("title").action());
    }

    public void testTarget() {
        assertEquals("title", new ActivateWindowEvent("title").target());
    }

    public void testParameters() {
        assertEquals("", new ActivateWindowEvent("title").parameters());
    }

    public void testScriptLine() {
        assertEquals("activate_window title", new ActivateWindowEvent("title").scriptLine());
    }

    private void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

    public void testPlay() throws InterruptedException, InvocationTargetException {
        final JFrame frameOne = new JFrame("title1");
        JFrame frameTwo = new JFrame("title2");
        frameTwo.setLocation(100,100);
        frameTwo.setVisible(true);
        waitForIdle();
        FocusListener focusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
                synchronized (FOCUS_LOCK) {
                    FOCUS_LOCK.notifyAll();
                }
            }

            public void focusLost(FocusEvent e) {
            }
        };
        frameOne.addFocusListener(focusListener);
        synchronized (FOCUS_LOCK) {
            frameOne.setVisible(true);
            FOCUS_LOCK.wait(100);
        }
        frameOne.removeFocusListener(focusListener);
        waitForIdle();
        ActivateWindowEvent event = new ActivateWindowEvent("title2");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findWindow").with(eq("title2"))
                .will(returnValue(frameTwo));
        event.play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        waitForIdle();
        assertFalse(frameOne.hasFocus());
        assertTrue(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner().toString(), frameTwo.hasFocus());
        frameOne.dispose();
        frameTwo.dispose();
    }

}

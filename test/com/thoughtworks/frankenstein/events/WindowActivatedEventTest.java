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
public class WindowActivatedEventTest extends MockObjectTestCase {
    public static final Object FOCUS_LOCK = new Object();

    public void testEqualsAndHashCode() {
        WindowActivatedEvent one = new WindowActivatedEvent("title");
        WindowActivatedEvent two = new WindowActivatedEvent("title");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("WindowActivatedEvent: title", new WindowActivatedEvent("title").toString());
    }

    public void testAction() {
        assertEquals("WindowActivated", new WindowActivatedEvent("title").action());
    }

    public void testTarget() {
        assertEquals("title", new WindowActivatedEvent("title").target());
    }

    public void testParameters() {
        assertEquals("", new WindowActivatedEvent("title").parameters());
    }

    public void testScriptLine() {
        assertEquals("WindowActivated title", new WindowActivatedEvent("title").scriptLine());
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
        WindowActivatedEvent event = new WindowActivatedEvent("title2");
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

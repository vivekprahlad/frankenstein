package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import javax.swing.*;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.common.WaitForIdle;

/**
 *  Ensures the behaviour of the AppletActivated Event.
 *
 * @author Pavan
 */
public class ActivateAppletEventTest extends MockObjectTestCase {
    public static final Object FOCUS_LOCK = new Object();
    private ActivateAppletEvent activateAppletEvent;

    protected void setUp() throws Exception {
        activateAppletEvent = new ActivateAppletEvent("testApplet");
    }

     public void testEqualsAndHashCode() {
        ActivateAppletEvent one = new ActivateAppletEvent("title");
        ActivateAppletEvent two = new ActivateAppletEvent("title");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("ActivateAppletEvent: title", new ActivateAppletEvent("title").toString());
    }

    public void testAction() {
        assertEquals("ActivateApplet", new ActivateAppletEvent("title").action());
    }

    public void testTarget() {
        assertEquals("title", new ActivateAppletEvent("title").target());
    }

    public void testParameters() {
        assertEquals("", new ActivateAppletEvent("title").parameters());
    }

    public void testScriptLine() {
        assertEquals("activate_applet \"title\"", new ActivateAppletEvent("title").scriptLine());
    }

    public void testPlay() throws InterruptedException {
        Frame frame = new Frame();
        JApplet applet = new JApplet();
        JButton button = new JButton();
        applet.setName("testApplet");
        frame.add(applet);
        frame.add(button);
        frame.setVisible(true);
        new WaitForIdle().waitForIdle();
        FocusListener listener = new FocusListener(){
            public void focusGained(FocusEvent e) {
                synchronized(FOCUS_LOCK){
                    FOCUS_LOCK.notifyAll();
                }
            }
            public void focusLost(FocusEvent e) {
            }
        };
        button.addFocusListener(listener);
        synchronized(FOCUS_LOCK){
            button.requestFocus();
            FOCUS_LOCK.wait(100);
        }
        assertTrue(button.hasFocus());
        assertFalse(applet.hasFocus());
        button.removeFocusListener(listener);
        applet.addFocusListener(listener);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findApplet").with(eq("testApplet"))
                .will(returnValue(applet));
        activateAppletEvent.play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        synchronized(FOCUS_LOCK){
            FOCUS_LOCK.wait(100);
        }
        mockComponentFinder.verify();
        assertTrue(applet.hasFocus());
        assertFalse(button.hasFocus());
        frame.dispose();
    }
}

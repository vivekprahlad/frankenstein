package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;

import com.thoughtworks.frankenstein.common.WaitForIdle;
import com.thoughtworks.frankenstein.application.ThreadUtil;
import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Ensures behaviour of keystroke event.
 */
public class KeyStrokeEventTest extends MockObjectTestCase implements FocusListener {
    private static final Object FOCUS_LOCK = new Object();
    private Robot robot;
    private JFrame frame;

    protected void setUp() throws Exception {
        super.setUp();
        waitForIdle();
        robot = new Robot();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        waitForIdle();
        robot = null;
    }

    private void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

    public void testEqualsAndHashCode() {
        KeyStrokeEvent one = new KeyStrokeEvent(KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_0);
        KeyStrokeEvent two = new KeyStrokeEvent(KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_0);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("KeyStrokeEvent: modifiers: Alt key: Ampersand", new KeyStrokeEvent(KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_AMPERSAND).toString());
    }

    public void testToStringWithoutModifires() {
        assertEquals("KeyStrokeEvent: key: Ampersand", new KeyStrokeEvent(0, KeyEvent.VK_AMPERSAND).toString());
    }

    public void testAction() {
        assertEquals("KeyStroke", new KeyStrokeEvent(KeyEvent.ALT_MASK, KeyEvent.VK_0).action());
    }

    public void testTarget() {
        assertEquals("", new KeyStrokeEvent(KeyEvent.ALT_MASK, KeyEvent.VK_0).target());
    }

    public void testParametersWithModifiers() {
        assertEquals("Alt+0", new KeyStrokeEvent(KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_0).parameters());
    }

    public void testParametersWithoutModifiers() {
        assertEquals("0", new KeyStrokeEvent(0, KeyEvent.VK_0).parameters());
    }

    public void testScriptLine() {
        assertEquals("key_stroke \"8,48\"", new KeyStrokeEvent(KeyEvent.ALT_MASK, KeyEvent.VK_0).scriptLine());
    }

    public void testPlayWithSpecialKeys() throws InterruptedException {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        frame = createAndShowFrame();
        modifierKeyTest(manager, KeyEvent.VK_ALT, KeyEvent.ALT_MASK);
        modifierKeyTest(manager, KeyEvent.VK_CONTROL, KeyEvent.CTRL_MASK);
        modifierKeyTest(manager, KeyEvent.VK_SHIFT, KeyEvent.SHIFT_MASK);
        frame.dispose();
    }

    public void testPlayWithNoSpecialKeys() throws InterruptedException {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        frame = createAndShowFrame();
        MockKeyEventDispatcher dispatcher = new MockKeyEventDispatcher();
        manager.addKeyEventDispatcher(dispatcher);
        Mock windowContext = mock(WindowContext.class);
        windowContext.expects(once()).method("focusOwner").will(returnValue(frame));
        new KeyStrokeEvent(0, KeyEvent.VK_A).play((WindowContext) windowContext.proxy(), null, null, robot);
        waitForIdle();
        List events = dispatcher.events;
        assertEquals(events.toString(), 3, events.size());
        assertKeyEvent(events, 0, KeyEvent.VK_A, KeyEvent.KEY_PRESSED);
        assertKeyEvent(events, 2, KeyEvent.VK_A, KeyEvent.KEY_RELEASED);
        manager.removeKeyEventDispatcher(dispatcher);
        frame.dispose();
    }

    private void modifierKeyTest(KeyboardFocusManager manager, int modifierKey, int modifierMask) {
        MockKeyEventDispatcher dispatcher = new MockKeyEventDispatcher();
        manager.addKeyEventDispatcher(dispatcher);
        Mock windowContext = mock(WindowContext.class);
        windowContext.expects(once()).method("focusOwner").will(returnValue(frame));
        new KeyStrokeEvent(modifierMask, KeyEvent.VK_A).play((WindowContext) windowContext.proxy(), null, null, robot);
        waitForIdle();
        List events = dispatcher.events;
        assertEquals(events.toString(), 5, events.size());
        assertKeyEvent(events, 0, modifierKey, KeyEvent.KEY_PRESSED);
        assertKeyEvent(events, 1, KeyEvent.VK_A, KeyEvent.KEY_PRESSED);
        assertKeyEvent(events, 3, KeyEvent.VK_A, KeyEvent.KEY_RELEASED);
        assertKeyEvent(events, 4, modifierKey, KeyEvent.KEY_RELEASED);
        manager.removeKeyEventDispatcher(dispatcher);
    }

    private void assertKeyEvent(List events, int index, int keyCode, int eventType) {
        KeyEvent event = (KeyEvent) events.get(index);
        assertEquals(event.toString(), keyCode, event.getKeyCode());
        assertEquals(eventType, event.getID());
    }

    private JFrame createAndShowFrame() throws InterruptedException {
        JFrame frame = new JFrame();
        frame.addFocusListener(this);
        frame.pack();
        synchronized(FOCUS_LOCK) {
            frame.setVisible(true);
            FOCUS_LOCK.wait();
        }
        frame.removeFocusListener(this);
        return frame;
    }

    public void focusGained(FocusEvent e) {
        synchronized(FOCUS_LOCK) {
            FOCUS_LOCK.notifyAll();
        }
    }

    public void focusLost(FocusEvent e) {
    }

    private class MockKeyEventDispatcher implements KeyEventDispatcher {
        private java.util.List events = new ArrayList();
        public boolean dispatchKeyEvent(KeyEvent e) {
            events.add(e);
            return false;
        }
    }
}

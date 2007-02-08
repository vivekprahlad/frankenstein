package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.KeyStrokeEvent;

/**
 * Ensures behaviour of KeyStrokeRecorder.
 */
public class KeyStrokeRecorderTest extends AbstractRecorderTestCase {
    private KeystrokeRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        recorder = new KeystrokeRecorder((Recorder) mockRecorder.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        recorder.unregister();
    }

    public void testRegister() {
        TestKeyboardFocusManager newManager = new TestKeyboardFocusManager();
        KeyboardFocusManager.setCurrentKeyboardFocusManager(newManager);
        int initialDispatcherCount = newManager.getKeyEventDispatcherCount();
        recorder.register();
        assertEquals(newManager.getKeyEventDispatcherCount(), initialDispatcherCount + 1);
    }

    public void testUnRegister() {
        TestKeyboardFocusManager newManager = new TestKeyboardFocusManager();
        KeyboardFocusManager.setCurrentKeyboardFocusManager(newManager);
        int initialDispatcherCount = newManager.getKeyEventDispatcherCount();
        recorder.register();
        recorder.unregister();
        assertEquals(newManager.getKeyEventDispatcherCount(), initialDispatcherCount);
    }

    public void testRecordsAltA() {
        mockRecorder.expects(once()).method("record").with(eq(new KeyStrokeEvent(KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_A)));
        recorder.dispatchKeyEvent(new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED, 3, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_A, 'a'));
    }

    public void testRecordsA() {
        mockRecorder.expects(once()).method("record").with(eq(new KeyStrokeEvent(0, KeyEvent.VK_A)));
        recorder.dispatchKeyEvent(new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED, 3, 0, KeyEvent.VK_A, 'a'));
    }

    public void testRecordsShiftA() {
        mockRecorder.expects(once()).method("record").with(eq(new KeyStrokeEvent(KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_A)));
        recorder.dispatchKeyEvent(new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED, 3, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_A, 'a'));
    }

    public void testRecordsControlA() {
        mockRecorder.expects(once()).method("record").with(eq(new KeyStrokeEvent(KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_A)));
        recorder.dispatchKeyEvent(new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED, 3, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_A, 'a'));
    }

    public void testFiltersControlKeys() {
        mockRecorder.expects(never()).method("record").with(ANYTHING);
        recorder.dispatchKeyEvent(new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED, 3, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED));
    }

    public void testFiltersAltKeys() {
        mockRecorder.expects(never()).method("record").with(ANYTHING);
        recorder.dispatchKeyEvent(new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED, 3, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_ALT, KeyEvent.CHAR_UNDEFINED));
    }

    public void testFiltersShiftKeys() {
        mockRecorder.expects(never()).method("record").with(ANYTHING);
        recorder.dispatchKeyEvent(new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED, 3, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED));
    }

    public void testDoesNotRecordEventsFromTextComponents() {
        recorder.dispatchKeyEvent(createEvent(new JTextField()));
        recorder.dispatchKeyEvent(createEvent(new JTextArea()));
        recorder.dispatchKeyEvent(createEvent(new JPasswordField()));
        recorder.dispatchKeyEvent(createEvent(new JEditorPane()));
    }

    private KeyEvent createEvent(JComponent source) {
        return new KeyEvent(source, 3, 3, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_0, '0');
    }


    private class TestKeyboardFocusManager extends DefaultKeyboardFocusManager {

        public synchronized int getKeyEventDispatcherCount() {
            java.util.List keyEventDispatchers = super.getKeyEventDispatchers();
            if (keyEventDispatchers==null) return 0;
            return keyEventDispatchers.size();
        }
    }
}

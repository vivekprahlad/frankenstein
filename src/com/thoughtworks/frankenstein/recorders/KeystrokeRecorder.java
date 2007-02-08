package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.text.JTextComponent;

import com.thoughtworks.frankenstein.events.KeyStrokeEvent;

/**
 * Understands recording keystrokes
 *
 * @author Vivek Prahlad
 */
public class KeystrokeRecorder implements ComponentRecorder, KeyEventDispatcher {
    private EventRecorder recorder;

    public KeystrokeRecorder(EventRecorder recorder) {
        this.recorder = recorder;
    }

    public void register() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public void unregister() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        if (!(e.getSource() instanceof JTextComponent) && e.getID() == KeyEvent.KEY_PRESSED && isNotModifierKey(e)) {
            recorder.record(new KeyStrokeEvent(e.getModifiersEx(), e.getKeyCode()));
        }
        return false;
    }

    protected boolean isNotModifierKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        return !(keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_SHIFT);
    }
}

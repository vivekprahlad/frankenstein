package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.Constants;
import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Represents a keystroke event.
 *
 * @author Vivek Prahlad
 */
public class KeyStrokeEvent extends AbstractFrankensteinEvent implements KeyListener {
    private int modifiers;
    private int keyCode;
    private int currentKey;

    public KeyStrokeEvent(int modifiers, int keyCode) {
        initEvent(modifiers, keyCode);
    }

    private void initEvent(int modifiers, int keyCode) {
        this.modifiers = modifiers;
        this.keyCode = keyCode;
        executeInPlayerThread();
    }

    public KeyStrokeEvent(String keyModifierAndKeyCodeText) {
        String[] keyEventParams = keyModifierAndKeyCodeText.split(Constants.SPACE);
        int modifiers = 0;
        int indexOfKeyCode = 0;
        if (keyEventParams.length == 2) {
            modifiers = KeyStrokeMap.getModifiersFromText(keyEventParams[0]);
            indexOfKeyCode = 1;
        }
        int keyCode = KeyStrokeMap.getKeyCodeFromText(keyEventParams[indexOfKeyCode]);
        initEvent(modifiers, keyCode);
    }


    public String toString() {
        return "KeyStrokeEvent:" + (modifiers != 0 ? " modifiers: " + KeyEvent.getModifiersExText(modifiers) : "") + " key: " + KeyEvent.getKeyText(keyCode);
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        list.addEvent(this);
    }

    public String target() {
        return "";
    }

    public String scriptLine() {
        String modifiersText = KeyEvent.getModifiersExText(modifiers);
        String modifiersTextPart = modifiersText;
        if (!Constants.EMPTY.equals(modifiersText)) {
            modifiersTextPart += Constants.SPACE;
        }
        return underscore(action()) + Constants.SPACE + quote(modifiersTextPart + KeyStrokeMap.getKeyText(keyCode));
    }

    public String parameters() {
        return (modifiers != 0 ? KeyEvent.getModifiersExText(modifiers) + "+" : "") + KeyEvent.getKeyText(keyCode);
    }

    private void releaseModifiers(Robot robot) {
        KeyEvent event = createKeyEvent();
        if (event.isAltDown()) releaseKey(robot, KeyEvent.VK_ALT);
        if (event.isAltGraphDown()) releaseKey(robot, KeyEvent.VK_ALT_GRAPH);
        if (event.isControlDown()) releaseKey(robot, KeyEvent.VK_CONTROL);
        if (event.isMetaDown()) releaseKey(robot, KeyEvent.VK_META);
        if (event.isShiftDown()) releaseKey(robot, KeyEvent.VK_SHIFT);
    }

    private void releaseKey(Robot robot, int key) {
        robot.keyRelease(key);
        currentKey = key;
    }

    private KeyEvent createKeyEvent() {
        return new KeyEvent(new JLabel(), 2, 0, modifiers, keyCode, 'a');
    }

    private void pressModifiers(Robot robot) {
        KeyEvent event = createKeyEvent();
        if (event.isAltDown()) robot.keyPress(KeyEvent.VK_ALT);
        if (event.isAltGraphDown()) robot.keyPress(KeyEvent.VK_ALT_GRAPH);
        if (event.isControlDown()) robot.keyPress(KeyEvent.VK_CONTROL);
        if (event.isMetaDown()) robot.keyPress(KeyEvent.VK_META);
        if (event.isShiftDown()) robot.keyPress(KeyEvent.VK_SHIFT);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public synchronized void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == currentKey) {
            notifyAll();
        }
    }

    public synchronized void run() {
        Component focusOwner = context.focusOwner();
        focusOwner.addKeyListener(this);
        pressKeys();
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        focusOwner.removeKeyListener(this);
    }

    private void pressKeys() {
        pressModifiers(robot);
        robot.keyPress(keyCode);
        releaseKey(robot, keyCode);
        releaseModifiers(robot);
    }
}

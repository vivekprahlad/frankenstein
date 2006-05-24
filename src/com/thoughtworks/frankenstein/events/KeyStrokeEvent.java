package com.thoughtworks.frankenstein.events;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Represents a keystroke event.
 * @author Vivek Prahlad
 */
public class KeyStrokeEvent extends AbstractFrankensteinEvent {
    private int modifiers;
    private int keyCode;

    public KeyStrokeEvent(int modifiers, int keyCode) {
        this.modifiers = modifiers;
        this.keyCode = keyCode;
    }

    public KeyStrokeEvent(String scriptLine) {
       this(keystrokeParams(scriptLine,0), keystrokeParams(scriptLine,1));
    }

    private static int keystrokeParams(String params, int index) {
        return Integer.parseInt(params.split(",")[index]);
    }

    public String toString() {
        return "KeyStrokeEvent:" +  (modifiers !=0 ? " modifiers: " + KeyEvent.getModifiersExText(modifiers) : "") + " key: " + KeyEvent.getKeyText(keyCode);
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        pressModifiers(robot);
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
        releaseModifiers(robot);
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        list.addEvent(this);
    }

    public String target() {
        return "";
    }

    public String scriptLine() {
        return action() + " " + modifiers + "," + keyCode;
    }

    public String parameters() {
        return (modifiers !=0 ? KeyEvent.getModifiersExText(modifiers) + "+" : "") + KeyEvent.getKeyText(keyCode);
    }

    private void releaseModifiers(Robot robot) {
        KeyEvent event = createKeyEvent();
        if (event.isAltDown()) robot.keyRelease(KeyEvent.VK_ALT);
        if (event.isAltGraphDown()) robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
        if (event.isControlDown()) robot.keyRelease(KeyEvent.VK_CONTROL);
        if (event.isMetaDown()) robot.keyRelease(KeyEvent.VK_META);
        if (event.isShiftDown()) robot.keyRelease(KeyEvent.VK_SHIFT);
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
}

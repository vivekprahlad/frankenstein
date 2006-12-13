package com.thoughtworks.frankenstein.events;

import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;
import com.thoughtworks.frankenstein.events.actions.ClickAction;

/**
 * Ensures behaviour of ButtonEvent
 */
public class ButtonEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        ButtonEvent eventOne = new ButtonEvent("parent.buttonName", new ClickAction());
        ButtonEvent eventTwo = new ButtonEvent("parent.buttonName", new ClickAction());
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("ButtonEvent: parent.buttonName", new ButtonEvent("parent.buttonName", new ClickAction()).toString());
    }

    public void testAction() {
        assertEquals("ClickButton", new ButtonEvent("testButton", new ClickAction()).action());
    }

    public void testTarget() {
        assertEquals("testButton", new ButtonEvent("testButton", new ClickAction()).target());
    }

    public void testParameters() {
        assertEquals("", new ButtonEvent("testButton", new ClickAction()).parameters());
    }

    public void testScriptLine() {
        assertEquals("click_button \"testButton\"", new ButtonEvent("testButton", new ClickAction()).scriptLine());
    }

    public void testPlaysEvent() throws InterruptedException, AWTException {
        JButton button = new JButton("abc");
        button.setSize(20,10);
        button.setName("parent.buttonName");
        Mock mockAction=mock(com.thoughtworks.frankenstein.events.actions.Action.class);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findComponent").with(ANYTHING, eq("parent.buttonName")).will(returnValue(button));
        mockAction.expects(once()).method("execute").with(eq(new Point(10,5)), ANYTHING, ANYTHING, ANYTHING);
        new ButtonEvent("parent.buttonName", (com.thoughtworks.frankenstein.events.actions.Action) mockAction.proxy()).play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
   }

    protected FrankensteinEvent createEvent() {
        return new ButtonEvent("parent.buttonName", new ClickAction());
    }
}

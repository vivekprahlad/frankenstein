package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.events.actions.*;

import javax.swing.*;
import java.awt.*;

/**
 * Ensures behaviour of RadioButtonEvent.
 */
public class RadioButtonEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        RadioButtonEvent eventOne = new RadioButtonEvent("parent.buttonName", new ClickAction());
        RadioButtonEvent eventTwo = new RadioButtonEvent("parent.buttonName", new ClickAction());
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("RadioButtonEvent: parent.buttonName", new RadioButtonEvent("parent.buttonName", new ClickAction()).toString());
    }

    public void testAction() {
        assertEquals("ClickRadioButton", new RadioButtonEvent("parent.buttonName", new ClickAction()).action());
    }

    public void testTarget() {
        assertEquals("parent.buttonName", new RadioButtonEvent("parent.buttonName", new ClickAction()).target());
    }

    public void testParameters() {
        assertEquals("", new RadioButtonEvent("parent.buttonName", new ClickAction()).parameters());
    }

    public void testScriptLine() {
        assertEquals("click_radio_button \"testButton\"", new RadioButtonEvent("testButton", new ClickAction()).scriptLine());
    }

    public void testPlaysEvent() {
        Mock mockComponentFinder = mock(ComponentFinder.class);
        RadioButtonEvent radioButtonEvent;
        MockAction mockAction=new MockAction();
        JRadioButton radioButton = new JRadioButton();
        radioButton.setSize(20,10);
        mockComponentFinder.expects(once()).method("findComponent").with(ANYTHING, eq("parent.buttonName")).will(returnValue(radioButton));
        radioButtonEvent=new RadioButtonEvent("parent.buttonName",  mockAction);
        radioButtonEvent.play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(radioButtonEvent.center(radioButton),mockAction.point);
    }

    protected FrankensteinEvent createEvent() {
        return new RadioButtonEvent("parent.buttonName", new ClickAction());
    }


}

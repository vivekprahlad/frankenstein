package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Ensures behaviour of CheckBoxSelectEvent.
 */
public class CheckboxEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        CheckboxEvent eventOne = new CheckboxEvent("parent.buttonName", true, new ClickAction());
        CheckboxEvent eventTwo = new CheckboxEvent("parent.buttonName", true, new ClickAction());
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("CheckboxEvent: parent.buttonName, selected: true", new CheckboxEvent("parent.buttonName", true, new ClickAction()).toString());
    }

    public void testAction() {
        assertEquals("ClickCheckbox", new CheckboxEvent("testButton", true, new ClickAction()).action());
    }

    public void testTarget() {
        assertEquals("testButton", new CheckboxEvent("testButton", true, new ClickAction()).target());
    }

    public void testParameters() {
        assertEquals("true", new CheckboxEvent("testButton", true, new ClickAction()).parameters());
    }

    public void testScriptLine() {
        assertEquals("click_checkbox \"testButton\" , \"true\"", new CheckboxEvent("testButton", true, new ClickAction()).scriptLine());
    }

    public void testPlaysEvent() {
        JCheckBox checkBox = new JCheckBox();
        CheckboxEvent checkBoxEvent;
        checkBox.setSize(20, 10);
        com.thoughtworks.frankenstein.events.actions.MockAction mockAction = new com.thoughtworks.frankenstein.events.actions.MockAction();
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findComponent").with(ANYTHING, eq("parent.buttonName")).will(returnValue(checkBox));
        checkBoxEvent = new CheckboxEvent("parent.buttonName", true, mockAction);
        checkBoxEvent.play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(checkBoxEvent.center(checkBox), mockAction.point);
    }

    protected FrankensteinEvent createEvent() {
        return new CheckboxEvent("parent.buttonName", true, new ClickAction());
    }

}

package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.events.actions.ClickAction;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.*;

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
        Mock mockAction=mock(com.thoughtworks.frankenstein.events.actions.Action.class);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findComponent").with(ANYTHING, eq("parent.buttonName")).will(returnValue(checkBox));
        mockAction.expects(once()).method("execute").with(eq(new Point(0,0)), ANYTHING, ANYTHING, ANYTHING);
        assertEquals(new Point(0,0),checkBox.getLocation());
        new CheckboxEvent("parent.buttonName", true,(com.thoughtworks.frankenstein.events.actions.Action) mockAction.proxy()).play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
       }

    protected FrankensteinEvent createEvent() {
        return new CheckboxEvent("parent.buttonName", true, new ClickAction());
    }
}

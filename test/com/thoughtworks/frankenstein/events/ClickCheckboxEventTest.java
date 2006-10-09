package com.thoughtworks.frankenstein.events;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import com.thoughtworks.frankenstein.playback.WindowContext;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Ensures behaviour of CheckBoxSelectEvent.
 */
public class ClickCheckboxEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        ClickCheckboxEvent eventOne = new ClickCheckboxEvent("parent.buttonName", true);
        ClickCheckboxEvent eventTwo = new ClickCheckboxEvent("parent.buttonName", true);
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("ClickCheckboxEvent: parent.buttonName, selected: true", new ClickCheckboxEvent("parent.buttonName", true).toString());
    }

    public void testAction() {
        assertEquals("ClickCheckbox", new ClickCheckboxEvent("testButton", true).action());
    }

    public void testTarget() {
        assertEquals("testButton", new ClickCheckboxEvent("testButton", true).target());
    }

    public void testParameters() {
        assertEquals("true", new ClickCheckboxEvent("testButton", true).parameters());
    }

    public void testScriptLine() {
        assertEquals("click_checkbox \"testButton\" , \"true\"", new ClickCheckboxEvent("testButton", true).scriptLine());
    }

    public void testPlay() {
        ClickCheckboxEvent event = new ClickCheckboxEvent("parent.buttonName", true);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockActionListener = mock(ActionListener.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JCheckBox checkBox = new JCheckBox();
        checkBox.addActionListener((ActionListener) mockActionListener.proxy());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.buttonName")).will(returnValue(checkBox));
        mockActionListener.expects(once()).method("actionPerformed");
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
    }

    protected FrankensteinEvent createEvent() {
        return new ClickCheckboxEvent("parent.buttonName", true);
    }
}

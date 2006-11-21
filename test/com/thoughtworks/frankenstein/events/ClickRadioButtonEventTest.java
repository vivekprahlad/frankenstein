package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Ensures behaviour of ClickRadioButtonEvent.
 */
public class ClickRadioButtonEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        ClickRadioButtonEvent eventOne = new ClickRadioButtonEvent("parent.buttonName");
        ClickRadioButtonEvent eventTwo = new ClickRadioButtonEvent("parent.buttonName");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("ClickRadioButtonEvent: parent.buttonName", new ClickRadioButtonEvent("parent.buttonName").toString());
    }

    public void testAction() {
        assertEquals("ClickRadioButton", new ClickRadioButtonEvent("parent.buttonName").action());
    }

    public void testTarget() {
        assertEquals("parent.buttonName", new ClickRadioButtonEvent("parent.buttonName").target());
    }

    public void testParameters() {
        assertEquals("", new ClickRadioButtonEvent("parent.buttonName").parameters());
    }

    public void testScriptLine() {
        assertEquals("click_radio_button \"testButton\"", new ClickRadioButtonEvent("testButton").scriptLine());
    }

    public void testPlaysEvent() {
        ClickRadioButtonEvent event = new ClickRadioButtonEvent("parent.buttonName");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockActionListener = mock(ActionListener.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JRadioButton radioButton = new JRadioButton();
        radioButton.addActionListener((ActionListener) mockActionListener.proxy());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.buttonName")).will(returnValue(radioButton));
        mockActionListener.expects(once()).method("actionPerformed");
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        waitForIdle();
    }

    protected FrankensteinEvent createEvent() {
        return new ClickRadioButtonEvent("parent.buttonName");
    }
}

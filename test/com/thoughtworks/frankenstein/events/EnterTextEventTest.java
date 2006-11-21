package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import org.jmock.Mock;

import javax.swing.*;

/**
 * Ensures behaviour of EnterTextEvent
 */
public class EnterTextEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        EnterTextEvent eventOne = new EnterTextEvent("parent.textFieldName", "text");
        EnterTextEvent eventTwo = new EnterTextEvent("parent.textFieldName", "text");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("EnterTextEvent: TextField: parent.textFieldName, text: text", new EnterTextEvent("parent.textFieldName", "text").toString());
    }

    public void testAction() {
        assertEquals("EnterText", new EnterTextEvent("parent.textFieldName", "text").action());
    }

    public void testTarget() {
        assertEquals("parent.textFieldName", new EnterTextEvent("parent.textFieldName", "text").target());
    }

    public void testParameters() {
        assertEquals("text", new EnterTextEvent("parent.textFieldName", "text").parameters());
    }

    public void testScriptLine() {
        assertEquals("enter_text \"parent.textFieldName\" , \"text\"", new EnterTextEvent("parent.textFieldName", "text").scriptLine());
    }

    public void testPlaysEvent() {
        EnterTextEvent event = new EnterTextEvent("parent.textFieldName", "text");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JTextField  textField = new JTextField();
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.textFieldName")).will(returnValue(textField));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        waitForIdle();
        assertEquals("text", textField.getText());
    }

    protected FrankensteinEvent createEvent() {
        return new EnterTextEvent("parent.textFieldName", "text");
    }
}

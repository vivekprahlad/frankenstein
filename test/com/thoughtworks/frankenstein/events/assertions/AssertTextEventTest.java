package com.thoughtworks.frankenstein.events.assertions;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.events.assertions.AssertTextEvent;
import com.thoughtworks.frankenstein.events.AbstractEventTestCase;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;

import javax.swing.*;

/**
 * Ensures behaviour of the check text event
 */
public class AssertTextEventTest extends AbstractEventTestCase {
    private JTextField field;

    public void testEqualsAndHashCode() {
        AssertTextEvent one = new AssertTextEvent("textField", "text");
        AssertTextEvent two = new AssertTextEvent("textField", "text");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
    }

    public void testAction() {
        assertEquals("AssertText", new AssertTextEvent("textField, text").action());
    }

    public void testTarget() {
        assertEquals("textField", new AssertTextEvent("textField", "text").target());
    }

    public void testParameters() {
        assertEquals("text", new AssertTextEvent("textField", "text").parameters());
    }

    public void testScriptLine() {
    }

    public void testPlay() throws Exception {
    }

    protected FrankensteinEvent createEvent() {
        return new AssertTextEvent("textField", "text");
    }

    public void testPlayWithMatchingText() {
        FrankensteinEvent event = new AssertTextEvent("textField", "text");
        Mock mockFinder = mock(ComponentFinder.class);
        field = new JTextField();
        field.setName("textField");
        field.setText("text");
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("textField")).will(returnValue(field));
        event.play(null, (ComponentFinder) mockFinder.proxy(), null, null);
    }

    public void testPlayWithTextNotMatching() {
        FrankensteinEvent event = new AssertTextEvent("textField", "text");
        Mock mockFinder = mock(ComponentFinder.class);
        field = new JTextField();
        field.setName("textField");
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("textField")).will(returnValue(field));
        try {
            event.play(null, (ComponentFinder) mockFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }
}

package com.thoughtworks.frankenstein.events;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;

/**
 * Ensures behaviour of the check text event
 */
public class CheckTextEventTest extends MockObjectTestCase {
    private JTextField field;

    public void testEqualsAndHashCode() {
        CheckTextEvent one = new CheckTextEvent("textField", "text");
        CheckTextEvent two = new CheckTextEvent("textField", "text");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testAction() {
        assertEquals("CheckText", new CheckTextEvent("textField, text").action());
    }

    public void testTarget() {
        assertEquals("textField", new CheckTextEvent("textField", "text").target());
    }

    public void testParameters() {
        assertEquals("text", new CheckTextEvent("textField", "text").parameters());
    }

    public void testPlayWithMatchingText() {
        FrankensteinEvent event = new CheckTextEvent("textField", "text");
        Mock mockFinder = mock(ComponentFinder.class);
        field = new JTextField();
        field.setName("textField");
        field.setText("text");
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("textField")).will(returnValue(field));
        event.play(null, (ComponentFinder) mockFinder.proxy(), null, null);
    }

    public void testPlayWithTextNotMatching() {
        FrankensteinEvent event = new CheckTextEvent("textField", "text");
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

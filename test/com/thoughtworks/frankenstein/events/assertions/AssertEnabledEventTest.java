package com.thoughtworks.frankenstein.events.assertions;

import com.thoughtworks.frankenstein.events.AbstractEventTestCase;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;

import org.jmock.Mock;

/**
 * Ensures behaviour of the check text event
 */
public class AssertEnabledEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        AssertEnabledEvent one = new AssertEnabledEvent("textField", true);
        AssertEnabledEvent two = new AssertEnabledEvent("textField", true);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("AssertEnabledEvent: textField, true" , new AssertEnabledEvent("textField", true).toString());
    }

    public void testAction() {
        assertEquals("AssertEnabled", new AssertEnabledEvent("textField", true).action());
    }

    public void testTarget() {
        assertEquals("textField", new AssertEnabledEvent("textField", true).target());
    }

    public void testParameters() {
        assertEquals("true", new AssertEnabledEvent("textField", true).parameters());
        assertEquals("false", new AssertEnabledEvent("textField", false).parameters());
    }

    public void testScriptLine() {
    }

    public void testPlay() throws Exception {
    }

    protected FrankensteinEvent createEvent() {
        return new AssertEnabledEvent("textField", true);
    }

    public void testPlayCheckMatching() {
        FrankensteinEvent event = new AssertEnabledEvent("textField", true);
        Mock mockFinder = mock(ComponentFinder.class);
        JTextField field = new JTextField();
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("textField")).will(returnValue(field));
        event.play(null, (ComponentFinder) mockFinder.proxy(), null, null);
    }

    public void testPlayCheckNotMatching() {
        FrankensteinEvent event = new AssertEnabledEvent("textField", false);
        Mock mockFinder = mock(ComponentFinder.class);
        JTextField field = new JTextField();
        mockFinder.expects(once()).method("findComponent").with(ANYTHING, eq("textField")).will(returnValue(field));
        try {
            event.play(null, (ComponentFinder) mockFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }
}

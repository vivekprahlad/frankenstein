package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 */
public class AssertLabelEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        AssertLabelEvent one = new AssertLabelEvent("labelValue");
        AssertLabelEvent two = new AssertLabelEvent("labelValue");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("AssertLabelEvent: labelValue", new AssertLabelEvent("labelValue").toString());
    }

    public void testAction() {
        assertEquals("AssertLabel", new AssertLabelEvent("labelValue").action());
    }

    public void testTarget() {
        assertEquals("", new AssertLabelEvent("labelValue").target());
    }

    public void testParameters() {
        assertEquals("labelValue", new AssertLabelEvent("labelValue").parameters());
    }

    public void testScriptLine() {
        assertEquals("assert_label \"labelValue\"", new AssertLabelEvent("labelValue").scriptLine());
    }

    public void testScriptLineInJava() {
        assertEquals("assertLabel(\"labelValue\")", new AssertLabelEvent("labelValue").scriptLine(new JavaScriptStrategy()));
    }

    public void testPlaysEvent() throws Exception {
        AssertLabelEvent event = new AssertLabelEvent("labelValue");
        JLabel lable = new JLabel("labelValue");
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findLabel").will(returnValue(lable));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
    }

    protected FrankensteinEvent createEvent() {
        return new AssertLabelEvent("labelValue");
    }
}

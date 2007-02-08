package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Created by IntelliJ IDEA.
 * User: cprakash
 * Date: Nov 22, 2006
 * Time: 3:53:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssertLabelEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        AssertLabelEvent one = new AssertLabelEvent("label", "labelValue");
        AssertLabelEvent two = new AssertLabelEvent("label", "labelValue");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("AssertLabelEvent: labelValue", new AssertLabelEvent("label", "labelValue").toString());
    }

    public void testAction() {
        assertEquals("AssertLabel", new AssertLabelEvent("label", "labelValue").action());
    }

    public void testTarget() {
        assertEquals("label", new AssertLabelEvent("label", "labelValue").target());
    }

    public void testParameters() {
        assertEquals("labelValue", new AssertLabelEvent("label", "labelValue").parameters());
    }

    public void testScriptLine() {
        assertEquals("assert_label \"label\" , \"labelValue\"", new AssertLabelEvent("label", "labelValue").scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        AssertLabelEvent event = new AssertLabelEvent("label", "labelValue");
        JLabel lable = new JLabel("labelValue");
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findLabel").will(returnValue(lable));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
    }

    protected FrankensteinEvent createEvent() {
        return new AssertLabelEvent("label", "labelValue");
    }
}

package com.thoughtworks.frankenstein.events.assertions;

import com.thoughtworks.frankenstein.events.AbstractEventTestCase;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

import javax.swing.*;

import org.jmock.Mock;

/**
 * Ensures behaviour of AssertEvent
 *
 * @author vivek
 */
public class AssertEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        AssertEvent one = new AssertEvent("table", "enabled", "true");
        AssertEvent two = new AssertEvent("table", "enabled", "true");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("AssertEvent: table, enabled: true",new AssertEvent("table", "enabled", "true").toString());
    }

    public void testAction() {
        assertEquals("Assert",new AssertEvent("table", "enabled", "true").action());
    }

    public void testTarget() {
        assertEquals("table",new AssertEvent("table", "enabled", "true").target());
    }

    public void testParameters() {
        assertEquals("enabled:true",new AssertEvent("table", "enabled", "true").parameters());
    }

    public void testScriptLine() {
        assertEquals("assert \"table\" , \"enabled:true\"", new AssertEvent("table", "enabled", "true").scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        AssertEvent event = new AssertEvent("table", "rowCount", "3");
        JTable table = new JTable(3, 3);
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findComponent").will(returnValue(table));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
    }

    public void testAssertWithWrongRowCount() {
        AssertEvent event = new AssertEvent("table", "rowCount", "2");
        JTable table = new JTable(3, 3);
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findComponent").will(returnValue(table));
        try {
            event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
            fail("Assertion should not have passed");
        } catch (Exception e) {
            assertEquals("Expected: 2, but was: 3", e.getCause().getCause().getMessage());
        }
    }

    public void testAssertTrueBooleanValue() {
        AssertEvent event = new AssertEvent("table", "enabled", "true");
        JTable table = new JTable(3, 3);
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findComponent").will(returnValue(table));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
    }

    public void testAssertFalseBooleanValue() {
        AssertEvent event = new AssertEvent("table", "enabled", "false");
        JTable table = new JTable(3, 3);
        table.setEnabled(false);
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findComponent").will(returnValue(table));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
    }

    public void testAssertsTableValues() {
        AssertEvent event = new AssertEvent("table", "getValueAt(0,0)", "12");
        JTable table = new JTable(3, 3) {
            public Object getValueAt(int row, int column) {
                if (row == 0 && column == 0) return new Integer(12);
                return "not defined";
            }
        };
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findComponent").will(returnValue(table));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
    }

    public void testAssertsRegularExpressions() {
        AssertEvent event = new AssertEvent("table", "rowCount", "regex:[0-9]");
        JTable table = new JTable(3, 3);
        Mock componentFinder = mock(ComponentFinder.class);
        componentFinder.expects(once()).method("findComponent").will(returnValue(table));
        event.play(null, (ComponentFinder) componentFinder.proxy(), null, null);
    }

    protected FrankensteinEvent createEvent() {
        return new AssertEvent("table", "enabled", "false");
    }
}

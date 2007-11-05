package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Ensures behaviour of StartTestEvent
 */
public class StartTestEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        StartTestEvent one = new StartTestEvent("testName");
        StartTestEvent two = new StartTestEvent("testName");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("StartTest: testName", new StartTestEvent("testName").toString());
    }

    public void testAction() {
        assertEquals("StartTest", new StartTestEvent("testName").action());
    }

    public void testTarget() {
        assertEquals("", new StartTestEvent("testName").target());
    }

    public void testParameters() {
        assertEquals("testName", new StartTestEvent("testName").parameters());
    }

    public void testScriptLine() {
        assertEquals("start_test \"testName\"", new StartTestEvent("testName").scriptLine());
    }

    public void testPlaysEvent() {
        Mock scriptContext = mock(ScriptContext.class);
        scriptContext.expects(once()).method("startTest").with(eq("testName"));
        new StartTestEvent("testName").play(null, null, (ScriptContext) scriptContext.proxy(), null);
    }

    protected FrankensteinEvent createEvent() {
        return new StartTestEvent("testName");
    }
}

package com.thoughtworks.frankenstein.events;

/**
 * Ensures behaviour of DelayEvent
 *
 * @author vivek
 */
public class DelayEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        DelayEvent one = new DelayEvent("10");
        DelayEvent two = new DelayEvent("10");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("DelayEvent: 10", new DelayEvent("10").toString());
    }

    public void testAction() {
        assertEquals("Delay", new DelayEvent("10").action());
    }

    public void testTarget() {
        assertEquals("10", new DelayEvent("10").target());
    }

    public void testParameters() {
        assertEquals("", new DelayEvent("10").parameters());
    }

    public void testScriptLine() {
        assertEquals("delay \"10\"", new DelayEvent("10").scriptLine());
    }

    public void testScriptLineInJava() {
        assertEquals("delay(10)", new DelayEvent("10").scriptLine(new JavaScriptStrategy()));
    }

    public void testPlaysEvent() throws Exception {
        long currentTime = System.currentTimeMillis();
        new DelayEvent("300").play(null, null, null, null);
        long executionTime = System.currentTimeMillis() - currentTime;
        assertTrue(executionTime > 250);
        assertTrue(executionTime < 350);
    }

    protected FrankensteinEvent createEvent() {
        return new DelayEvent("100");
    }
}

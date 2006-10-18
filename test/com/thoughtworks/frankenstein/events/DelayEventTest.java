package com.thoughtworks.frankenstein.events;

/**
 * Ensures behaviour of DelayEvent
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

    }

    public void testAction() {
    }

    public void testTarget() {
    }

    public void testParameters() {
    }

    public void testScriptLine() {
    }

    public void testPlay() throws Exception {
        long currentTime = System.currentTimeMillis();
        new DelayEvent("100").play(null, null, null, null);
        long executionTime = System.currentTimeMillis() - currentTime;
        assertTrue(executionTime > 100);
        assertTrue(executionTime < 110);
    }

    protected FrankensteinEvent createEvent() {
        return new DelayEvent("100");
    }
}

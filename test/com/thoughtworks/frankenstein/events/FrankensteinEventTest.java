package com.thoughtworks.frankenstein.events;

import junit.framework.TestCase;

/**
 * Ensures behaviour of the null object
 */
public class FrankensteinEventTest extends TestCase {
    public void testPlay() {
        try {
            FrankensteinEvent.NULL.play(null, null, null, null);
            fail();
        } catch (UnsupportedOperationException e) {
            //Expected;
        }
    }

    public void testTarget() {
        assertEquals("NullTarget", FrankensteinEvent.NULL.target());
    }
}

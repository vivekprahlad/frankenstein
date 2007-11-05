package com.thoughtworks.frankenstein.application;

import junit.framework.TestCase;

/**
 * Ensures the behaviour of the FI factory.
 */
public class FrankensteinIntegrationFactoryTest extends TestCase {
    public void testFactoryGivesTheCorrectInstanceOfFIBasedOnSystemProperty() {
        FrankensteinIntegration frankensteinIntegration = FrankensteinIntegrationFactory.getFrankensteinIntegration(Object.class);
        assertEquals(DefaultFrankensteinIntegration.class, frankensteinIntegration.getClass());
        frankensteinIntegration.stop();
        System.setProperty("playback", "step");
        assertEquals(PlaybackFrankensteinIntegration.class, FrankensteinIntegrationFactory.getFrankensteinIntegration(Object.class).getClass());
    }
}
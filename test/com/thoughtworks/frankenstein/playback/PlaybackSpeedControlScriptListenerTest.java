package com.thoughtworks.frankenstein.playback;

import junit.framework.TestCase;

/**
 * Ensures the behaviour of the PlaybackSpeedControllScriptListener.
 *
 * @author Pavan
 * @author Prakash
 */

public class PlaybackSpeedControlScriptListenerTest extends TestCase {
    private PlaybackSpeedControlScriptListener playbackSpeedControlScriptListener;
    private long defaultDelay;
    private static int accuracyErrorOffset = 100;

    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        defaultDelay = 1000;
        playbackSpeedControlScriptListener = new PlaybackSpeedControlScriptListener(defaultDelay, false);
    }

    public void testListenerShouldNotWaitIfShouldWaitIsFalse() {
        assertTrue(actualMethodDelay() < (defaultDelay + accuracyErrorOffset));
    }

    public void testListenerWaitsAtleastSelectedIntervalBeforeReturning() {
        playbackSpeedControlScriptListener.setShouldWaitBetweenSteps(true);
        assertTrue(actualMethodDelay() >= (defaultDelay - accuracyErrorOffset));
    }

    private long actualMethodDelay() {
        long startTime = System.currentTimeMillis();
        playbackSpeedControlScriptListener.scriptStepStarted(0);
        return (System.currentTimeMillis() - startTime);
    }
}

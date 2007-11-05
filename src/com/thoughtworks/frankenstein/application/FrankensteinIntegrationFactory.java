package com.thoughtworks.frankenstein.application;

/**
 * Understands creating the right DefaultFrankensteinIntegration
 */
public class FrankensteinIntegrationFactory {
    private static final String PLAYBACK_PROPERTY = "playback";
    private static final String STEP_VALUE = "step";

    public static FrankensteinIntegration getFrankensteinIntegration(Class mainClass) {
        String playbackProperty = System.getProperty(PLAYBACK_PROPERTY);
        if (playbackProperty != null && playbackProperty.equals(STEP_VALUE)) {
            return new PlaybackFrankensteinIntegration(mainClass);
        }
        return new DefaultFrankensteinIntegration(mainClass);
    }
}

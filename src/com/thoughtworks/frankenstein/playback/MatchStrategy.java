package com.thoughtworks.frankenstein.playback;

/**
 * Understands matching strings
 *
 * @author vivek
 */
public abstract class MatchStrategy {

    public static boolean matchValues(String actual, String expected) {
        return expected.startsWith("regex:") ? matchRegularExpression(actual, expected) : actual.equals(expected);
    }

    private static boolean matchRegularExpression(String actual, String expected) {
        return actual.matches(expected.substring(6, expected.length()));
    }
}

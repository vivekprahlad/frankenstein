package com.thoughtworks.frankenstein.playback;

/**
 * Understands matching strings
 *
 * @author vivek
 */
public class MatchStrategy {
    private static final String REGEX = "regex:";

    public static boolean matchValues(String actual, String expected) {
        return expected.startsWith(REGEX) ? matchRegularExpression(actual, expected) : actual.equals(expected);
    }

    private static boolean matchRegularExpression(String actual, String expected) {
        return actual.matches(expected.substring(REGEX.length(), expected.length()));
    }
}

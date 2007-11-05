package com.thoughtworks.frankenstein.events;

/**
 * Understands how to convert an event into a script line
 */
public interface ScriptStrategy {
    String convert(String[] scriptElements);
}

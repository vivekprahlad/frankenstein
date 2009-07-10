package com.thoughtworks.frankenstein.events;

/**
 * Understands how to convert an event into a script line
 */
public interface ScriptStrategy {
    
    String convert(String[] scriptElements);

    String toMethod(String action);

    String enclose(String body);

    String escape(boolean value);

    String escape(int value);

    String array(String[] strings);

    String array(int[] rows);

    String cell(int row, int column);
}

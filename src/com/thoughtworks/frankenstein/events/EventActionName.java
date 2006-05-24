package com.thoughtworks.frankenstein.events;

/**
 * Creates action names from event names.
 * @author Vivek Prahlad
 */
public class EventActionName {

    public static String action(Class eventClass) {
        String[] strings = eventClass.getName().split("\\.");
        return strings[strings.length-1].replaceAll("Event", "");
    }
}

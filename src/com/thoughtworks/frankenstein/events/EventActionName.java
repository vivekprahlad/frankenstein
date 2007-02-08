package com.thoughtworks.frankenstein.events;

/**
 * Creates eventActionName names from event names.
 *
 * @author Vivek Prahlad
 */
public class EventActionName {

    public static String eventActionName(Class eventClass) {
        return actionInternal(eventClass, "Event");
    }

    public static String action(Class eventClass) {
        return actionInternal(eventClass, "Action");
    }

    private static String actionInternal(Class actionClass, String suffix) {
        String[] strings = className(actionClass);
        return strings[strings.length - 1].replaceAll(suffix, "");
    }

    private static String[] className(Class actionClass) {
        return actionClass.getName().split("\\.");
    }
}

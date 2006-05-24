package com.thoughtworks.frankenstein.events;

/**
 * Registers event types, creates events from script lines.
 * @author Vivek Prahlad
 */
public interface EventRegistry {
    void registerEvent(Class eventClass);
    FrankensteinEvent createEvent(String scriptLine);
}

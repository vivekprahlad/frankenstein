package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Represents a list view of recorded events.
 *
 * @author Vivek Prahlad
 */
public interface EventList {
    public void addEvent(FrankensteinEvent event);

    public void removeLastEvent();

    public void replaceLastEvent(FrankensteinEvent event);
}

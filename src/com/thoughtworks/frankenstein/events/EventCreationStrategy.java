package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.events.actions.Action;

import java.lang.reflect.Constructor;

/**
 * Understands SOMETHING
 *
 * @author vivek
 */
abstract class EventCreationStrategy {
    protected static final EventCreationStrategy SIMPLE_EVENT_CREATION_STRATEGY = new EventCreationStrategy() {
        protected FrankensteinEvent createEvent(Class eventClass, String scriptLine, Action action) {
            try {
                Constructor constructor = eventClass.getConstructor(new Class[]{String.class});
                return (FrankensteinEvent) constructor.newInstance(new Object[]{scriptLine});
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error", e);
            }
        }

    };
    protected static final EventCreationStrategy COMPOUND_EVENT_CREATION_STRATEGY = new EventCreationStrategy() {
        protected FrankensteinEvent createEvent(Class eventClass, String scriptLine, Action action) {
            try {
                Constructor constructor = eventClass.getConstructor(new Class[]{String.class, Action.class});
                return (FrankensteinEvent) constructor.newInstance(new Object[]{scriptLine, action});
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error", e);
            }
        }
    };

    protected abstract FrankensteinEvent createEvent(Class eventClass, String scriptLine, Action action);
}

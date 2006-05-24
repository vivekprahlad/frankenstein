package com.thoughtworks.frankenstein.events;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * The default event registry
 * @author Vivek Prahlad
 */
public class DefaultEventRegistry implements EventRegistry{
    private Map eventNameToEventClassMap = new HashMap();

    public DefaultEventRegistry() {
        registerEvent(CancelTableEditEvent.class);
        registerEvent(CheckTextEvent.class);
        registerEvent(ClickButtonEvent.class);
        registerEvent(ClickCheckboxEvent.class);
        registerEvent(ClickRadioButtonEvent.class);
        registerEvent(DialogShownEvent.class);
        registerEvent(EditTableCellEvent.class);
        registerEvent(EnterTextEvent.class);
        registerEvent(InternalFrameShownEvent.class);
        registerEvent(KeyStrokeEvent.class);
        registerEvent(NavigateEvent.class);
        registerEvent(SelectDropDownEvent.class);
        registerEvent(SelectListEvent.class);
        registerEvent(SelectTreeEvent.class);
        registerEvent(StartTestEvent.class);
        registerEvent(StopTableEditEvent.class);
        registerEvent(SwitchTabEvent.class);
        registerEvent(WindowActivatedEvent.class);
    }

    public void registerEvent(Class eventClass) {
        checkClass(eventClass);
        eventNameToEventClassMap.put(EventActionName.action(eventClass), eventClass);
    }

    private void checkClass(Class eventClass) {
        if (!FrankensteinEvent.class.isAssignableFrom(eventClass)) throw new IllegalArgumentException("Registered class is not a FrankensteinEvent");
        checkHasStringConstructor(eventClass);
    }

    private void checkHasStringConstructor(Class eventClass) {
        try {
            eventClass.getConstructor(new Class[] {String.class});
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Event class does not have string constructor");
        }
    }

    public FrankensteinEvent createEvent(String scriptLine) {
        String[] strings = scriptLine.split("\\s", 2);
        if (!eventNameToEventClassMap.containsKey(strings[0])) throw new RuntimeException("Could not find event for action :" + strings[0]);
        return createEvent((Class) eventNameToEventClassMap.get(strings[0]), strings[1]);
    }

    private FrankensteinEvent createEvent(Class clazz, String string) {
        try {
            Constructor constructor = clazz.getConstructor(new Class[]{String.class});
            return (FrankensteinEvent) constructor.newInstance(new Object[]{string});
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }
}

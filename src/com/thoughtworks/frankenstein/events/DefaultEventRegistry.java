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
        registerEvent(ActivateInternalFrameEvent.class);
        registerEvent(ActivateWindowEvent.class);
        registerEvent(CancelTableEditEvent.class);
        registerEvent(AssertTextEvent.class);
        registerEvent(ClickButtonEvent.class);
        registerEvent(ClickCheckboxEvent.class);
        registerEvent(ClickRadioButtonEvent.class);
        registerEvent(CloseInternalFrameEvent.class);
        registerEvent(DialogShownEvent.class);
        registerEvent(EditTableCellEvent.class);
        registerEvent(EnterTextEvent.class);
        registerEvent(InternalFrameShownEvent.class);
        registerEvent(KeyStrokeEvent.class);
        registerEvent(NavigateEvent.class);
        registerEvent(SelectDropDownEvent.class);
        registerEvent(SelectFileEvent.class);
        registerEvent(SelectListEvent.class);
        registerEvent(SelectTreeEvent.class);
        registerEvent(StartTestEvent.class);
        registerEvent(StopTableEditEvent.class);
        registerEvent(SwitchTabEvent.class);
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
        int quoteIndex = scriptLine.indexOf("\"");
        String line = scriptLine.substring(quoteIndex).replaceAll("\"\\s+,\\s+\"", " ").replaceAll("\"", "");
        String action = convert(scriptLine.substring(0, quoteIndex-1));
        if (!eventNameToEventClassMap.containsKey(action)) throw new RuntimeException("Could not find event for action :" + action);
        return createEvent((Class) eventNameToEventClassMap.get(action), line);
    }

    private FrankensteinEvent createEvent(Class clazz, String string) {
        try {
            Constructor constructor = clazz.getConstructor(new Class[]{String.class});
            return (FrankensteinEvent) constructor.newInstance(new Object[]{string});
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }

    protected String convert(String scriptAction) {
        String[] tokens = scriptAction.split("_");
        String converted = "";
        for (int i = 0; i < tokens.length; i++) {
            converted += capitalize(tokens[i]);
        }
        return converted;
    }

    private String capitalize(String token) {
        return Character.toUpperCase(token.charAt(0)) + token.substring(1);
    }
}

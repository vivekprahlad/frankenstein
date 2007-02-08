package com.thoughtworks.frankenstein.events;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.frankenstein.events.actions.Action;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.events.assertions.AssertEvent;
import com.thoughtworks.frankenstein.script.Script;

/**
 * The default event registry
 *
 * @author Vivek Prahlad
 */
public class DefaultEventRegistry implements EventRegistry {
    private Map eventNameToEventClassMap = new HashMap();
    private Map actionNameToActionClassMap = new HashMap();

    public DefaultEventRegistry() {
        registerAction(ClickAction.class);
        registerAction(RightClickAction.class);
        registerAction(DoubleClickAction.class);
        registerEvent(ActivateInternalFrameEvent.class);
        registerEvent(ActivateDialogEvent.class);
        registerEvent(ActivateWindowEvent.class);
        registerEvent(AssertEvent.class);
        registerEvent(AssertLabelEvent.class);
        registerEvent(CancelTableEditEvent.class);
        registerEvent(ButtonEvent.class);
        registerEvent(CheckboxEvent.class);
        registerEvent(RadioButtonEvent.class);
        registerEvent(TableHeaderEvent.class);
        registerEvent(CloseAllDialogsEvent.class);
        registerEvent(CloseInternalFrameEvent.class);
        registerEvent(DelayEvent.class);
        registerEvent(DialogClosedEvent.class);
        registerEvent(DialogShownEvent.class);
        registerEvent(ListEvent.class);
        registerEvent(EditTableCellEvent.class);
        registerEvent(EnterTextEvent.class);
        registerEvent(InternalFrameShownEvent.class);
        registerEvent(KeyStrokeEvent.class);
        registerEvent(NavigateEvent.class);
        registerEvent(TableRowEvent.class);
        registerEvent(TreeEvent.class);
        registerEvent(SelectDropDownEvent.class);
        registerEvent(SelectFileEvent.class);
        registerEvent(SelectFilesEvent.class);
        registerEvent(SelectListEvent.class);
        registerEvent(SelectTableRowEvent.class);
        registerEvent(SelectTreeEvent.class);
        registerEvent(StartTestEvent.class);
        registerEvent(StopTableEditEvent.class);
        registerEvent(SwitchTabEvent.class);
        registerEvent(MoveSliderEvent.class);
    }

    public void registerEvent(Class eventClass) {
        checkClass(eventClass);
        eventNameToEventClassMap.put(EventActionName.eventActionName(eventClass), eventClass);
    }

    public void registerAction(Class actionClazz) {
        checkActionClass(actionClazz);
        actionNameToActionClassMap.put(EventActionName.action(actionClazz), actionClazz);
    }

    private void checkActionClass(Class actionClass) {
        if (!Action.class.isAssignableFrom(actionClass))
            throw new IllegalArgumentException("Registered class " + actionClass.getClass().getName() + " is not an Action");
    }

    private void checkClass(Class eventClass) {
        if (!FrankensteinEvent.class.isAssignableFrom(eventClass))
            throw new IllegalArgumentException("Registered class is not a FrankensteinEvent");
        checkHasStringConstructor(eventClass);
    }

    private void checkHasStringConstructor(Class eventClass) {
        try {
            eventClass.getConstructor(new Class[]{String.class});
        } catch (NoSuchMethodException e) {
            checkHasCompoundConstructor(eventClass);
        }
    }

    private void checkHasCompoundConstructor(Class eventClass) {
        try {
            eventClass.getConstructor(new Class[]{String.class, Action.class});
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Event class does not have string or compound constructor");
        }
    }

    public FrankensteinEvent createEvent(String rawLine) {
        String scriptLine = convert(rawLine);
        if (isCompoundEvent(scriptLine)) {
            return createCompoundEvent(scriptLine);
        } else {
            return createEvent(scriptLine, EventCreationStrategy.SIMPLE_EVENT_CREATION_STRATEGY);
        }
    }

    private FrankensteinEvent createCompoundEvent(String rawLine) {
        String[] scriptLine = split(rawLine);
        Action action = createAction(scriptLine[0]);
        return createEvent(action, scriptLine[1], EventCreationStrategy.COMPOUND_EVENT_CREATION_STRATEGY);
    }

    private FrankensteinEvent createEvent(String rawLine, EventCreationStrategy strategy) {
        return createEvent(null, rawLine, strategy);
    }

    private FrankensteinEvent createEvent(Action action, String rawLine, EventCreationStrategy strategy) {
        String scriptLine = Script.unescapeNewLines(rawLine);
        int quoteIndex = scriptLine.indexOf("\"");
        String line = "", actionName = "";
        if (quoteIndex == -1) {
            actionName = scriptLine;
        } else {
            line = scriptLine.substring(quoteIndex).replaceAll("\"\\s+,\\s+\"", " ").replaceAll("\"", "");
            actionName = scriptLine.substring(0, quoteIndex - 1);
        }
        verifyActionRegistered(actionName);
        return strategy.createEvent((Class) eventNameToEventClassMap.get(actionName), line, action);
    }

    protected String[] split(String scriptLine) {
        Set set = actionNameToActionClassMap.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            String actionName = (String) iterator.next();
            if (scriptLine.startsWith(actionName)) {
                return new String[]{actionName, scriptLine.substring(actionName.length(), scriptLine.length())};
            }
        }
        throw new RuntimeException("No matching action found");
    }

    protected Action createAction(String scriptLine) {
        for (Iterator iterator = actionNameToActionClassMap.keySet().iterator(); iterator.hasNext();) {
            String actionName = (String) iterator.next();
            if (scriptLine.startsWith(actionName))
                return createAction((Class) actionNameToActionClassMap.get(actionName));
        }
        throw new RuntimeException("Could not create action from scriptline: " + scriptLine);
    }

    private Action createAction(Class actionClass) {
        try {
            Constructor constructor = actionClass.getConstructor(new Class[]{});
            return (Action) constructor.newInstance(new Object[]{});
        } catch (Exception e) {
            throw new RuntimeException("Could not create action: ", e);
        }
    }

    protected boolean isCompoundEvent(String scriptLine) {
        for (Iterator iterator = actionNameToActionClassMap.keySet().iterator(); iterator.hasNext();) {
            if (scriptLine.startsWith((String) iterator.next())) return true;
        }
        return false;
    }

    private void verifyActionRegistered(String action) {
        if (!eventNameToEventClassMap.containsKey(action))
            throw new RuntimeException("Could not find event for eventActionName :" + action);
    }

    protected String convert(String scriptAction) {
        String[] line = scriptAction.split("\\s+");
        String remaining = scriptAction.substring(line[0].length(), scriptAction.length());
        String[] tokens = line[0].split("_");
        String action = "";
        for (int i = 0; i < tokens.length; i++) {
            action += capitalize(tokens[i]);
        }
        return action + remaining;
    }

    private String capitalize(String token) {
        return Character.toUpperCase(token.charAt(0)) + token.substring(1);
    }
}

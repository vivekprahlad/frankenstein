package com.thoughtworks.frankenstein.events;

import java.awt.*;

import junit.framework.TestCase;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Ensures behaviour of the default event registry
 */
public class DefaultEventRegistryTest extends TestCase {
    private DefaultEventRegistry defaultEventRegistry;

    protected void setUp() throws Exception {
        defaultEventRegistry = new DefaultEventRegistry();
        new EventActionName();
    }

    public void testChecksWhetherClassImplementsFrankensteinEventInterface() {
        try {
            defaultEventRegistry.registerEvent(DefaultEventRegistry.class);
            fail();
        } catch (Exception e) {
        }
    }

    public void testChecksIfClassHasSingleStringConstructor() {
        try {
            defaultEventRegistry.registerEvent(FakeFrankensteinEvent.class);
            fail();
        } catch (Exception e) {
        }
    }

    public void testCreatesCancelTableEditEvent() {
        defaultEventRegistry.registerEvent(CancelTableEditEvent.class);
        assertEquals(new CancelTableEditEvent("abc"), defaultEventRegistry.createEvent("CancelTableEdit abc"));
    }

    public void testCreatesClickButtonEvent() {
       defaultEventRegistry.registerEvent(ClickButtonEvent.class);
       assertEquals(new ClickButtonEvent("abc"), defaultEventRegistry.createEvent("click_button abc"));
    }

    public void testCreatesClickCheckboxEvent() {
        defaultEventRegistry.registerEvent(ClickCheckboxEvent.class);
        assertEquals(new ClickCheckboxEvent("abc", true), defaultEventRegistry.createEvent("ClickCheckbox abc true"));
    }

    public void testCreatesClickRadioButtonEvent() {
        defaultEventRegistry.registerEvent(ClickRadioButtonEvent.class);
        assertEquals(new ClickRadioButtonEvent("abc"), defaultEventRegistry.createEvent("ClickRadioButton abc"));
    }

    public void testCreatesDialogShownEvent() {
        defaultEventRegistry.registerEvent(DialogShownEvent.class);
        assertEquals(new DialogShownEvent("abc"), defaultEventRegistry.createEvent("DialogShown abc"));
    }

    public void testCreatesEditTableCellEvent() {
        defaultEventRegistry.registerEvent(EditTableCellEvent.class);
        assertEquals(new EditTableCellEvent("abc", 1, 2), defaultEventRegistry.createEvent("EditTableCell abc 1,2"));
    }

    public void testCreatesEnterTextEvent() {
        defaultEventRegistry.registerEvent(EnterTextEvent.class);
        assertEquals(new EnterTextEvent("textField", "abc"), defaultEventRegistry.createEvent("EnterText textField abc"));
    }

    public void testCreatesInternalFrameShownEvent() {
        defaultEventRegistry.registerEvent(InternalFrameShownEvent.class);
        assertEquals(new InternalFrameShownEvent("title"), defaultEventRegistry.createEvent("InternalFrameShown title"));
    }

    public void testCreatesKeystrokeEvent() {
        defaultEventRegistry.registerEvent(KeyStrokeEvent.class);
        assertEquals(new KeyStrokeEvent(0,48), defaultEventRegistry.createEvent("KeyStroke 0,48"));
    }

    public void testCreatesNavigateEvent() {
        defaultEventRegistry.registerEvent(NavigateEvent.class);
        assertEquals(new NavigateEvent("one>two"), defaultEventRegistry.createEvent("Navigate one>two"));
    }

    public void testCreatesSelectDropDownEvent() {
        defaultEventRegistry.registerEvent(SelectDropDownEvent.class);
        assertEquals(new SelectDropDownEvent("dropDown", "value"), defaultEventRegistry.createEvent("SelectDropDown dropDown value"));
    }

    public void testCreatesSelectListEvent() {
        defaultEventRegistry.registerEvent(SelectListEvent.class);
        assertEquals(new SelectListEvent("list", "value"), defaultEventRegistry.createEvent("SelectList list value"));
    }

    public void testCreatesSelectTreeEvent() {
        defaultEventRegistry.registerEvent(SelectTreeEvent.class);
        assertEquals(new SelectTreeEvent("tree", "one>two>three"), defaultEventRegistry.createEvent("SelectTree tree one>two>three"));
    }

    public void testCreatesStopTableEditEvent() {
        defaultEventRegistry.registerEvent(StopTableEditEvent.class);
        assertEquals(new StopTableEditEvent("table"), defaultEventRegistry.createEvent("StopTableEdit table"));
    }

    public void testCreatesSwitchTabEvent() {
        defaultEventRegistry.registerEvent(SwitchTabEvent.class);
        assertEquals(new SwitchTabEvent("tabName", "tabTitle"), defaultEventRegistry.createEvent("SwitchTab tabName tabTitle"));
    }

    public void testCreatesWindowActivatedEvent() {
        defaultEventRegistry.registerEvent(ActivateWindowEvent.class);
        assertEquals(new ActivateWindowEvent("title"), defaultEventRegistry.createEvent("ActivateWindow title"));
    }

    public void testCreatesCheckTextEvent() {
        defaultEventRegistry.registerEvent(CheckTextEvent.class);
        assertEquals(new CheckTextEvent("textField", "text"), defaultEventRegistry.createEvent("CheckText textField text"));
    }

    public void testCreatesStartTestEvent() {
        defaultEventRegistry.registerEvent(StartTestEvent.class);
        assertEquals(new StartTestEvent("testName"), defaultEventRegistry.createEvent("StartTest testName"));
    }

    public void testThrowsExceptionIfCreatingEventFails() {
        defaultEventRegistry.registerEvent(ExceptionThrowingEvent.class);
        try {
            defaultEventRegistry.createEvent("ExceptionThrowing textField text");
            fail();
        } catch(Exception e) {
        }
    }

    public void testConvertsUnderscoreTextToCamelCase() {
        assertEquals("ClickButton", defaultEventRegistry.convert("click_button"));
    }

    public void testReturnsCamelCaseAsCamelCase() {
        assertEquals("ClickButton", defaultEventRegistry.convert("ClickButton"));
    }

    private class FakeFrankensteinEvent implements FrankensteinEvent {

        public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        }

        public void record(EventList list, FrankensteinEvent lastEvent) {
        }

        public String action() {
            return null;
        }

        public String target() {
            return null;
        }

        public String parameters() {
            return null;
        }

        public String scriptLine() {
            return null;
        }
    }

}

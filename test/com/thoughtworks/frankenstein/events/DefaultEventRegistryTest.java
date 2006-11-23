package com.thoughtworks.frankenstein.events;

import java.awt.*;

import junit.framework.TestCase;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.EventList;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.events.assertions.AssertEvent;

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
        assertEquals(new CancelTableEditEvent("abc"), defaultEventRegistry.createEvent("CancelTableEdit \"abc\""));
    }

    public void testCreatesClickButtonEvent() {
       defaultEventRegistry.registerEvent(ClickButtonEvent.class);
       assertEquals(new ClickButtonEvent("abc"), defaultEventRegistry.createEvent("click_button \"abc\""));
    }

    public void testCreatesClickCheckboxEvent() {
        defaultEventRegistry.registerEvent(ClickCheckboxEvent.class);
        assertEquals(new ClickCheckboxEvent("abc", true), defaultEventRegistry.createEvent("ClickCheckbox \"abc\" \"true\""));
    }

    public void testCreatesClickRadioButtonEvent() {
        defaultEventRegistry.registerEvent(ClickRadioButtonEvent.class);
        assertEquals(new ClickRadioButtonEvent("abc"), defaultEventRegistry.createEvent("ClickRadioButton \"abc\""));
    }

    public void testCreatesDialogShownEvent() {
        defaultEventRegistry.registerEvent(DialogShownEvent.class);
        assertEquals(new DialogShownEvent("2"), defaultEventRegistry.createEvent("DialogShown \"2\""));
    }

    public void testCreatesEditTableCellEvent() {
        defaultEventRegistry.registerEvent(EditTableCellEvent.class);
        assertEquals(new EditTableCellEvent("abc", 1, 2), defaultEventRegistry.createEvent("EditTableCell \"abc\" \"1,2\""));
    }

    public void testCreatesEnterTextEvent() {
        defaultEventRegistry.registerEvent(EnterTextEvent.class);
        assertEquals(new EnterTextEvent("textField", "abc"), defaultEventRegistry.createEvent("EnterText \"textField\" \"abc\""));
    }

    public void testCreatesInternalFrameShownEvent() {
        defaultEventRegistry.registerEvent(InternalFrameShownEvent.class);
        assertEquals(new InternalFrameShownEvent("title"), defaultEventRegistry.createEvent("InternalFrameShown \"title\""));
    }

    public void testCreatesKeystrokeEvent() {
        defaultEventRegistry.registerEvent(KeyStrokeEvent.class);
        assertEquals(new KeyStrokeEvent(0,48), defaultEventRegistry.createEvent("KeyStroke \"0,48\""));
    }

    public void testCreatesNavigateEvent() {
        defaultEventRegistry.registerEvent(NavigateEvent.class);
        assertEquals(new NavigateEvent("one>two"), defaultEventRegistry.createEvent("Navigate \"one>two\""));
    }

    public void testCreatesSelectDropDownEvent() {
        defaultEventRegistry.registerEvent(SelectDropDownEvent.class);
        assertEquals(new SelectDropDownEvent("dropDown", "value"), defaultEventRegistry.createEvent("SelectDropDown \"dropDown\" \"value\""));
    }

    public void testCreatesSelectListEvent() {
        defaultEventRegistry.registerEvent(SelectListEvent.class);
        assertEquals(new SelectListEvent("list", new String[]{"value"}), defaultEventRegistry.createEvent("SelectList \"list\" \"value\""));
    }

    public void testCreatesSelectTreeEvent() {
        defaultEventRegistry.registerEvent(SelectTreeEvent.class);
        assertEquals(new SelectTreeEvent("tree", "one>two>three"), defaultEventRegistry.createEvent("SelectTree \"tree\" \"one>two>three\""));
    }

    public void testCreatesStopTableEditEvent() {
        defaultEventRegistry.registerEvent(StopTableEditEvent.class);
        assertEquals(new StopTableEditEvent("table"), defaultEventRegistry.createEvent("StopTableEdit \"table\""));
    }

    public void testCreatesSwitchTabEvent() {
        defaultEventRegistry.registerEvent(SwitchTabEvent.class);
        assertEquals(new SwitchTabEvent("tabName", "tabTitle"), defaultEventRegistry.createEvent("SwitchTab \"tabName\" \"tabTitle\""));
    }

    public void testCreatesWindowActivatedEvent() {
        defaultEventRegistry.registerEvent(ActivateWindowEvent.class);
        assertEquals(new ActivateWindowEvent("title"), defaultEventRegistry.createEvent("ActivateWindow \"title\""));
    }

    public void testCreatesAssertEvent() {
           defaultEventRegistry.registerEvent(AssertEvent.class);
           assertEquals(new AssertEvent("table", "enabled", "true"), defaultEventRegistry.createEvent("Assert \"table\" \"enabled:true\""));
    }

    public void testCreatesStartTestEvent() {
        defaultEventRegistry.registerEvent(StartTestEvent.class);
        assertEquals(new StartTestEvent("testName"), defaultEventRegistry.createEvent("StartTest \"testName\""));
    }

    public void testCreateDelayEvent() {
        defaultEventRegistry.registerEvent(DelayEvent.class);
        assertEquals(new DelayEvent("10"), defaultEventRegistry.createEvent("Delay \"10\""));
    }
    public void testCreateDoubleClickListEvent() {
        defaultEventRegistry.registerEvent(DoubleClickListEvent.class);
        assertEquals(new DoubleClickListEvent("listone",0), defaultEventRegistry.createEvent("DoubleClickList \"listone\" 0"));
    }

    public void testCreateClickTableHeaderEvent() {
        defaultEventRegistry.registerEvent(ClickTableHeaderEvent.class);
        assertEquals(new ClickTableHeaderEvent("header","one"), defaultEventRegistry.createEvent( "click_table_header \"header\" , \"one\""));
    }

    public void testCreateRightClickTableRows() {
        defaultEventRegistry.registerEvent(RightClickTableRowsEvent.class);
        assertEquals(new RightClickTableRowsEvent("table",1), defaultEventRegistry.createEvent( "right_click_table_rows \"table\" , \"1\""));
    }

     public void testCreateAssertLabel() {
        defaultEventRegistry.registerEvent(AssertLabelEvent.class);
        assertEquals(new AssertLabelEvent("label","labelValue"), defaultEventRegistry.createEvent("assert_label \"label\" , \"labelValue\""));
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

    public void testCreatesCloseAllDialogsEvent() {
        defaultEventRegistry.registerEvent(CloseAllDialogsEvent.class);
        assertEquals(new CloseAllDialogsEvent(""), defaultEventRegistry.createEvent("CloseAllDialogs \"\""));
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

        public void run() {
        }
    }

}

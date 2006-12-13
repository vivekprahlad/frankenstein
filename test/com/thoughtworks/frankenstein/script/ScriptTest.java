package com.thoughtworks.frankenstein.script;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.frankenstein.events.*;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.events.assertions.AssertEvent;
import junit.framework.TestCase;


/**
 * Ensures behaviour of script file. This is an integration test for saving and loading
 * tests.
 */
public class ScriptTest extends TestCase {
    private static final String SCRIPT =
    "assert \"table\" , \"enabled:true\"\n"+
    "cancel_table_edit \"tableName\"\n"+
    "click_button \"click button\"\n" +
    "click_checkbox \"check Box\" , \"true\"\n" +
    "click_radio_button \"radio button\"\n" +
    "dialog_shown \"2\"\n" +
    "enter_text \"textFieldName\" , \"one&#xA;two\"\n" +
    "edit_table_cell \"tableName\" , \"1,1\"\n" +
    "enter_text \"textBox\" , \"text\"\n" +
    "internal_frame_shown \"title a\"\n" +
    "key_stroke \"0,48\"\n" +
    "navigate \"ab>ac>de\"\n" +
    "select_drop_down \"combo\" , \"text a\"\n" +
    "select_list \"list\" , \"text a\"\n" +
    "stop_table_edit \"table\"\n" +
    "switch_tab \"tab\" , \"text a\"\n" +
    "activate_window \"text a\"\n"+
    "double_click_list \"list\" , \"0\"\n"+
    "click_table_header \"header\" , \"one\"\n"+
    "right_click_table_row \"table\" , \"1\"\n"+
    "assert_label \"label\" , \"labelValue\"\n"+
    "move_slider \"slider\" , \"10\"";

    public void testCreatesScriptFromEventList() {
        List eventList = new ArrayList();
        eventList.add(new AssertEvent("table","enabled", "true"));
        eventList.add(new CancelTableEditEvent("tableName"));
        eventList.add(new ButtonEvent("click button", new ClickAction()));
        eventList.add(new CheckboxEvent("check Box", true, new ClickAction()));
        eventList.add(new RadioButtonEvent("radio button", new ClickAction()));
        eventList.add(new DialogShownEvent("2"));
        eventList.add(new EnterTextEvent("textFieldName", "one\ntwo"));
        eventList.add(new EditTableCellEvent("tableName", 1, 1));
        eventList.add(new EnterTextEvent("textBox", "text"));
        eventList.add(new InternalFrameShownEvent("title a"));
        eventList.add(new KeyStrokeEvent(0, 48));
        eventList.add(new NavigateEvent("ab>ac>de"));
        eventList.add(new SelectDropDownEvent("combo", "text a"));
        eventList.add(new SelectListEvent("list", new String[] {"text a"}));
        eventList.add(new StopTableEditEvent("table"));
        eventList.add(new SwitchTabEvent("tab", "text a"));
        eventList.add(new ActivateWindowEvent("text a"));
        eventList.add(new DoubleClickListEvent("list",0));
        eventList.add(new TableHeaderEvent("header","one", new ClickAction()));
        eventList.add(new TableRowEvent("table",1, new RightClickAction()));
        eventList.add(new AssertLabelEvent("label","labelValue"));
        eventList.add(new MoveSliderEvent("slider",10));
        Script script = new Script(new DefaultEventRegistry());
        assertEquals(SCRIPT, script.scriptText(eventList));
    }

    public void testCreatesEventListFromScript() throws IOException {
        Script script = new Script(new DefaultEventRegistry());
        List eventList = script.parse(new StringReader(SCRIPT));
        assertEquals(22, eventList.size());
        assertEquals(new AssertEvent("table","enabled", "true"),eventList.get(0));
        assertEquals(new CancelTableEditEvent("tableName"), eventList.get(1));
        assertEquals(new ButtonEvent("click button", new ClickAction()), eventList.get(2));
        assertEquals(new CheckboxEvent("check Box", true, new ClickAction()), eventList.get(3));
        assertEquals(new RadioButtonEvent("radio button", new ClickAction()), eventList.get(4));
        assertEquals(new DialogShownEvent("2"), eventList.get(5));
        assertEquals(new EnterTextEvent("textFieldName", "one\ntwo"), eventList.get(6));
        assertEquals(new EditTableCellEvent("tableName", 1, 1), eventList.get(7));
        assertEquals(new EnterTextEvent("textBox", "text"), eventList.get(8));
        assertEquals(new InternalFrameShownEvent("title a"), eventList.get(9));
        assertEquals(new KeyStrokeEvent(0, 48), eventList.get(10));
        assertEquals(new NavigateEvent("ab>ac>de"), eventList.get(11));
        assertEquals(new SelectDropDownEvent("combo", "text a"), eventList.get(12));
        assertEquals(new SelectListEvent("list", new String[]{"text a"}), eventList.get(13));
        assertEquals(new StopTableEditEvent("table"), eventList.get(14));
        assertEquals(new SwitchTabEvent("tab", "text a"), eventList.get(15));
        assertEquals(new ActivateWindowEvent("text a"), eventList.get(16));
        assertEquals(new DoubleClickListEvent("list",0),eventList.get(17));
        assertEquals(new TableHeaderEvent("header","one", new ClickAction()),eventList.get(18));
        assertEquals(new TableRowEvent("table",1, new RightClickAction()),eventList.get(19));
        assertEquals(new AssertLabelEvent("label","labelValue"),eventList.get(20));
        assertEquals(new MoveSliderEvent("slider",10),eventList.get(21));
    }
}

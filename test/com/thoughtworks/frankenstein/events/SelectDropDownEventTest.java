package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

import javax.swing.*;

/**
 * Ensures behaviour of SelectDropDownEvent
 */
public class SelectDropDownEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        SelectDropDownEvent eventOne = new SelectDropDownEvent("parent.comboFieldName", "text");
        SelectDropDownEvent eventTwo = new SelectDropDownEvent("parent.comboFieldName", "text");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("SelectDropDownEvent: Combo: parent.comboFieldName, Choice: text", new SelectDropDownEvent("parent.comboFieldName", "text").toString());
    }

    public void testAction() {
        assertEquals("SelectDropDown", new SelectDropDownEvent("parent.comboFieldName", "text").action());
    }

    public void testTarget() {
        assertEquals("parent.comboFieldName", new SelectDropDownEvent("parent.comboFieldName", "text").target());
    }

    public void testParameters() {
        assertEquals("text", new SelectDropDownEvent("parent.comboFieldName", "text").parameters());
    }

    public void testScriptLine() {
        assertEquals("select_drop_down \"parent.comboFieldName\" , \"text\"", new SelectDropDownEvent("parent.comboFieldName", "text").scriptLine());
    }

    public void testPlaysEvent() {
        SelectDropDownEvent event = new SelectDropDownEvent("parent.comboFieldName", "text");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JComboBox  combo = new JComboBox(new Object[]{"one", "text", "three"});
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.comboFieldName")).will(returnValue(combo));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals("text", combo.getSelectedItem());
    }

    public void testPlaysWithRegularExpression() {
        SelectDropDownEvent event = new SelectDropDownEvent("parent.comboFieldName", "regex:tex.*");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JComboBox  combo = new JComboBox(new Object[]{"one", "text", "three"});
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.comboFieldName")).will(returnValue(combo));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals("text", combo.getSelectedItem());
    }

    protected FrankensteinEvent createEvent() {
        return new SelectDropDownEvent("parent.comboFieldName", "text");
    }
}

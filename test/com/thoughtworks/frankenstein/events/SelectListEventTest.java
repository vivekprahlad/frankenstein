package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Ensures behaviour of SelectListEvent
 */
public class SelectListEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        SelectListEvent eventOne = new SelectListEvent("parent.listFieldName", new String[] {"text"});
        SelectListEvent eventTwo = new SelectListEvent("parent.listFieldName", new String[] {"text"});
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("SelectListEvent: List: parent.listFieldName, Values: text", new SelectListEvent("parent.listFieldName", new String[] {"text"}).toString());
    }

    public void testAction() {
        assertEquals("SelectList", new SelectListEvent("parent.listFieldName", new String[] {"text"}).action());
    }

    public void testTarget() {
        assertEquals("parent.listFieldName", new SelectListEvent("parent.listFieldName", new String[] {"text"}).target());
    }

    public void testParameters() {
        assertEquals("text", new SelectListEvent("parent.listFieldName", new String[] {"text"}).parameters());
    }

    public void testScriptLine() {
        assertEquals("select_list \"parent.listFieldName\" , \"text\"", new SelectListEvent("parent.listFieldName", new String[] {"text"}).scriptLine());
    }

    public void testPlaysEvent() {
        SelectListEvent event = new SelectListEvent("parent.listFieldName", new String[] {"text"});
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JList  list = new JList(new Object[]{"one", "text", "three"});
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.listFieldName")).will(returnValue(list));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals("text", list.getSelectedValue());
    }

    public void testPlayWithMultipleElementsSelected() {
        SelectListEvent event = new SelectListEvent("parent.listFieldName", new String[] {"one", "two"});
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JList  list = new JList(new Object[]{"one", "two", "three"});
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.listFieldName")).will(returnValue(list));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(2, list.getSelectedValues().length);
        assertEquals("one", list.getSelectedValues()[0]);
        assertEquals("two", list.getSelectedValues()[1]);
    }

    protected FrankensteinEvent createEvent() {
        return new SelectListEvent("parent.listFieldName", new String[] {"text"});
    }

    public void testPlayWithNonExistentListItemDoesNotSelect() {
        SelectListEvent event = new SelectListEvent("parent.listFieldName", new String[] {"non_existent_text"});
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JList  list = new JList(new Object[]{"one", "text", "three"});
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.listFieldName")).will(returnValue(list));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(null, list.getSelectedValue());
    }
}

package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;

/**
 * Ensures behaviour of SelectListEvent
 */
public class SelectListEventTest extends MockObjectTestCase {

    public void testEqualsAndHashCode() {
        SelectListEvent eventOne = new SelectListEvent("parent.listFieldName", "text");
        SelectListEvent eventTwo = new SelectListEvent("parent.listFieldName", "text");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("SelectListEvent: List: parent.listFieldName, Value: text", new SelectListEvent("parent.listFieldName", "text").toString());
    }

    public void testAction() {
        assertEquals("SelectList", new SelectListEvent("parent.listFieldName", "text").action());
    }

    public void testTarget() {
        assertEquals("parent.listFieldName", new SelectListEvent("parent.listFieldName", "text").target());
    }

    public void testParameters() {
        assertEquals("text", new SelectListEvent("parent.listFieldName", "text").parameters());
    }

    public void testScriptLine() {
        assertEquals("select_list \"parent.listFieldName\" \"text\"", new SelectListEvent("parent.listFieldName", "text").scriptLine());
    }

    public void testPlay() {
        SelectListEvent event = new SelectListEvent("parent.listFieldName", "text");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        DefaultWindowContext context = new DefaultWindowContext();
        JList  list = new JList(new Object[]{"one", "text", "three"});
        mockComponentFinder.expects(once()).method("findComponent").with(eq(context), eq("parent.listFieldName")).will(returnValue(list));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals("text", list.getSelectedValue());
    }

    public void testPlayWithNonExistentListItemDoesNotSelect() {
        SelectListEvent event = new SelectListEvent("parent.listFieldName", "nonexisten_text");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        DefaultWindowContext context = new DefaultWindowContext();
        JList  list = new JList(new Object[]{"one", "text", "three"});
        mockComponentFinder.expects(once()).method("findComponent").with(eq(context), eq("parent.listFieldName")).will(returnValue(list));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(null, list.getSelectedValue());
    }
}

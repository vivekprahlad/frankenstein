package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.events.actions.MockAction;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;

/**

 */
public class ListEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        ListEvent eventOne = new ListEvent("list", 0, new DoubleClickAction());
        ListEvent eventTwo = new ListEvent("list", 0, new DoubleClickAction());
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("ListEvent: list 0", new ListEvent("list", 0, new DoubleClickAction()).toString());
    }

    public void testAction() {
        assertEquals("DoubleClickList", new ListEvent("listone", 0, new DoubleClickAction()).action());
    }

    public void testTarget() {
        assertEquals("listone", new ListEvent("listone", 0, new DoubleClickAction()).target());
    }

    public void testParameters() {
        assertEquals("0", new ListEvent("listone", 0, new DoubleClickAction()).parameters());
    }

    public void testScriptLine() {
        assertEquals("double_click_list \"list\" , \"0\"", new ListEvent("list", 0, new DoubleClickAction()).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        String[] listOfStrings = {"one", "two", "three", "four", "five"};
        JList list = new JList(listOfStrings);
        list.setName("listName");
        MockAction mockAction = new com.thoughtworks.frankenstein.events.actions.MockAction();
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findComponent").with(ANYTHING, eq("listName")).will(returnValue(list));
        new ListEvent("listName", 0, mockAction).play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(0, list.locationToIndex(mockAction.point));

    }

    protected FrankensteinEvent createEvent() {
        return new ListEvent("listName", 0, new DoubleClickAction());
    }


}

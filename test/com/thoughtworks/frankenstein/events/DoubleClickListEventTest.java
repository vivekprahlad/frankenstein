package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: sbhat
 * Date: Nov 13, 2006
 * Time: 4:50:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleClickListEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        DoubleClickListEvent eventOne = new DoubleClickListEvent("list", 0);
        DoubleClickListEvent eventTwo = new DoubleClickListEvent("list", 0);
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("DoubleClickListEvent: list 0", new DoubleClickListEvent("list", 0).toString());
    }

    public void testAction() {
        assertEquals("DoubleClickList", new DoubleClickListEvent("listone", 0).action());
    }

    public void testTarget() {
        assertEquals("listone", new DoubleClickListEvent("listone", 0).target());
    }

    public void testParameters() {
        assertEquals("0", new DoubleClickListEvent("listone", 0).parameters());
    }

    public void testScriptLine() {
        assertEquals("double_click_list \"list\" , \"0\"", new DoubleClickListEvent("list", 0).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        String[] listOfStrings = {"one", "two", "three", "four", "five"};
        JList list = new JList(listOfStrings);
        list.setName("listName");
        DoubleClickListEvent event = new DoubleClickListEvent("listName", 0);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        MockMouseListener mockMouseListener = new MockMouseListener();
        list.addMouseListener(mockMouseListener);
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("listName")).will(returnValue(list));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
        assertTrue(mockMouseListener.doubleClicked);
        assertFalse(mockMouseListener.popupTrigger);
    }

    protected FrankensteinEvent createEvent() {
        return new DoubleClickListEvent("listName", 0);
    }

    private class MockMouseListener extends MouseAdapter {
        private boolean doubleClicked = false;
        private boolean popupTrigger = true;
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) doubleClicked = true;
            popupTrigger = e.isPopupTrigger();
        }
    }
}

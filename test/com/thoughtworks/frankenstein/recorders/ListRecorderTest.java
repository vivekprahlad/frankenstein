package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.ListEvent;
import com.thoughtworks.frankenstein.events.actions.DoubleClickAction;
import com.thoughtworks.frankenstein.events.actions.RightClickAction;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of ListRecorder.
 */
public class ListRecorderTest extends AbstractRecorderTestCase {
    private JList list;
    private ListRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        String[] listOfStrings = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        list = new JList(listOfStrings);
        list.setName("listName");
        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        new JFrame().getContentPane().add(scrollPane);
        recorder = new ListRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsMouseListenerWhenListIsShown() {
        int listenerCount = listenerCount();
        recorder.componentShown(list);
        assertTrue(listenerCount() == listenerCount + 1);
    }

    public void testRemovesMouseListenerWhenListIsHidden() {
        int listenerCount = listenerCount();
        recorder.componentShown(list);
        recorder.componentHidden(list);
        assertTrue(listenerCount() == listenerCount);
    }

    public void testRecordsDoubleClickOnListFirst() {
        recorder.componentShown(list);
        mockRecorder.expects(once()).method("record").with(eq(new ListEvent("listName", 0, new DoubleClickAction())));
        Point point = list.indexToLocation(0);
        recorder.listMouseListener.mouseClicked(new MouseEvent(list, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 2, false));
    }


    public void testRecordsDoubleClickOnListWhenTheItemNotInView() {
        recorder.componentShown(list);
        mockRecorder.expects(once()).method("record").with(eq(new ListEvent("listName", 9, new RightClickAction())));
        Point point = list.indexToLocation(9);
        recorder.listMouseListener.mouseClicked(new MouseEvent(list, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 1, true));
    }

    private int listenerCount() {
        return list.getMouseListeners().length;
    }
}
          
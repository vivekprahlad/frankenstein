package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.events.DoubleClickListEvent;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: sbhat
 * Date: Nov 14, 2006
 * Time: 2:41:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleClickListRecorderTest extends AbstractRecorderTestCase {
    private JList list;
    private DoubleClickListRecorder recorder;
    private String[] listOfStrings;


    protected void setUp() throws Exception {
        super.setUp();
        listOfStrings= new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        list = new JList(listOfStrings);
        list.setName("listName");
        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        new JFrame().getContentPane().add(scrollPane);
        recorder = new DoubleClickListRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
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
        mockRecorder.expects(once()).method("record").with(eq(new DoubleClickListEvent("listName", 0)));
        Point point = list.indexToLocation(0);
        recorder.mouseClicked(new MouseEvent(list, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 2, false));
    }


    public void testRecordsDoubleClickOnListWhenTheItemNotInView() {
        recorder.componentShown(list);
        mockRecorder.expects(once()).method("record").with(eq(new DoubleClickListEvent("listName", 9)));
        Point point = list.indexToLocation(9);
        recorder.mouseClicked(new MouseEvent(list, MouseEvent.MOUSE_CLICKED, 0, 0, point.x, point.y, 2, false));
    }

    private int listenerCount() {
        return list.getMouseListeners().length;
    }
}

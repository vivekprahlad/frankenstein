package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cprakash
 * Date: Nov 20, 2006
 * Time: 5:24:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClickTableHeaderEventTest extends AbstractEventTestCase {


    public void testEqualsAndHashCode() {
        ClickTableHeaderEvent eventOne = new ClickTableHeaderEvent("header", "one");
        ClickTableHeaderEvent eventTwo = new ClickTableHeaderEvent("header", "one");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("ClickTableHeaderEvent: header one", new ClickTableHeaderEvent("header", "one").toString());
    }

    public void testAction() {
        assertEquals("ClickTableHeader", new ClickTableHeaderEvent("header", "one").action());
    }

    public void testTarget() {
        assertEquals("header", new ClickTableHeaderEvent("header", "one").target());
    }

    public void testParameters() {
        assertEquals("one", new ClickTableHeaderEvent("header", "one").parameters());
    }

    public void testScriptLine() {
        assertEquals("click_table_header \"header\" , \"one\"", new ClickTableHeaderEvent("header", "one").scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JTable table = new JTable(3, 3);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setName("header");
        tableHeader.getColumnModel().getColumn(1).setHeaderValue("one");
        ClickTableHeaderEvent event = new ClickTableHeaderEvent("header", "one");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        MockMouseListener mockMouseListener = new MockMouseListener();
        tableHeader.addMouseListener(mockMouseListener);
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("header")).will(returnValue(tableHeader));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
        assertTrue(mockMouseListener.singleClicked);
        assertFalse(mockMouseListener.popupTrigger);
    }

    protected FrankensteinEvent createEvent() {
        return new ClickTableHeaderEvent("header", "one");

    }

    private class MockMouseListener extends MouseAdapter {
        private boolean singleClicked = false;
        private boolean popupTrigger = true;

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1&&(e.getButton()==MouseEvent.BUTTON1)) {
                singleClicked = true;

            }
            popupTrigger = e.isPopupTrigger();
        }
    }
}

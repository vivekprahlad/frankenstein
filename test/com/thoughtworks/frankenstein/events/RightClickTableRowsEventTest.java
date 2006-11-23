package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Ensures RightClickTableRowsEvent works as expected
 */
public class RightClickTableRowsEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        RightClickTableRowsEvent eventOne = new RightClickTableRowsEvent("table", 1);
        RightClickTableRowsEvent eventTwo = new RightClickTableRowsEvent("table", 1);
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("RightClickTableRowsEvent: table 1", new RightClickTableRowsEvent("table", 1).toString());
    }

    public void testAction() {
        assertEquals("RightClickTableRows", new RightClickTableRowsEvent("table", 1).action());
    }

    public void testTarget() {
        assertEquals("table", new RightClickTableRowsEvent("table", 1).target());
    }

    public void testParameters() {
        assertEquals("1", new RightClickTableRowsEvent("table", 1).parameters());
    }

    public void testScriptLine() {
        assertEquals("right_click_table_rows \"table\" , \"1\"", new RightClickTableRowsEvent("table", 1).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JTable table = new JTable(3, 3);
        RightClickTableRowsEvent event = new RightClickTableRowsEvent("table", 1);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        Mock mockMouseListener = mock(MouseListener.class);
        table.addMouseListener((MouseListener) mockMouseListener.proxy());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("table")).will(returnValue(table));
        mockMouseListener.expects(once()).method("mouseClicked");
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
      }

    protected FrankensteinEvent createEvent() {
        return new RightClickTableRowsEvent("table",1);  //To change body of implemented methods use File | Settings | File Templates.
    }


}

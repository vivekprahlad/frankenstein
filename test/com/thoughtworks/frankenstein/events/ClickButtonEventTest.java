package com.thoughtworks.frankenstein.events;

import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.common.RobotFactory;

/**
 * Ensures behaviour of ClickButtonEvent
 */
public class ClickButtonEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        ClickButtonEvent eventOne = new ClickButtonEvent("parent.buttonName");
        ClickButtonEvent eventTwo = new ClickButtonEvent("parent.buttonName");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("ClickButtonEvent: parent.buttonName", new ClickButtonEvent("parent.buttonName").toString());
    }

    public void testAction() {
        assertEquals("ClickButton", new ClickButtonEvent("testButton").action());
    }

    public void testTarget() {
        assertEquals("testButton", new ClickButtonEvent("testButton").target());
    }

    public void testParameters() {
        assertEquals("", new ClickButtonEvent("testButton").parameters());
    }

    public void testScriptLine() {
        assertEquals("click_button \"testButton\"", new ClickButtonEvent("testButton").scriptLine());
    }

    public void testPlaysEvent() throws InterruptedException, AWTException {
        JButton button = new JButton("abc");
        button.setName("parent.buttonName");
        ClickButtonEvent event = new ClickButtonEvent("parent.buttonName");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockActionListener = mock(ActionListener.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        mockContext.expects(once()).method("addWindowContextListener");
        mockContext.expects(once()).method("removeWindowContextListener");
        button.addActionListener((ActionListener) mockActionListener.proxy());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.buttonName")).will(returnValue(button));
        mockActionListener.expects(once()).method("actionPerformed");
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
    }

    protected FrankensteinEvent createEvent() {
        return new ClickButtonEvent("parent.buttonName"); 
    }
}

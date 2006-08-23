package com.thoughtworks.frankenstein.events;

import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;

/**
 * Ensures behaviour of ClickButtonEvent
 */
public class ClickButtonEventTest extends MockObjectTestCase {

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
        assertEquals("click_button testButton", new ClickButtonEvent("testButton").scriptLine());
    }

    public void testPlay() throws InterruptedException, AWTException {
        JFrame frame = new JFrame();
        JButton button = new JButton("abc");
        frame.getContentPane().add(button);
        frame.pack();
        frame.setVisible(true);
        ClickButtonEvent event = new ClickButtonEvent("parent.buttonName");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockActionListener = mock(ActionListener.class);
        DefaultWindowContext context = new DefaultWindowContext();
        button.addActionListener((ActionListener) mockActionListener.proxy());
        mockComponentFinder.expects(once()).method("findComponent").with(eq(context), eq("parent.buttonName")).will(returnValue(button));
        mockActionListener.expects(once()).method("actionPerformed");
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, new Robot());
        frame.dispose();
    }
}

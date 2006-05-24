package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Ensures behaviour of CheckBoxSelectEvent.
 */
public class NavigateEventTest extends MockObjectTestCase {

    public void testEqualsAndHashCode() {
        NavigateEvent eventOne = new NavigateEvent("toplevel>nextlevel>thirdlevel");
        NavigateEvent eventTwo = new NavigateEvent("toplevel>nextlevel>thirdlevel");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("NavigateEvent: toplevel>nextlevel>thirdlevel", new NavigateEvent("toplevel>nextlevel>thirdlevel").toString());
    }

    public void testPlay() {
        NavigateEvent event = new NavigateEvent("toplevel>nextlevel>thirdlevel");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockActionListener = mock(ActionListener.class);
        DefaultWindowContext context = new DefaultWindowContext();
        JMenuItem item = new JMenuItem("thirdlevel");
        item.addActionListener((ActionListener) mockActionListener.proxy());
        mockComponentFinder.expects(once()).method("findMenuItem").with(eq(context), eq("toplevel>nextlevel>thirdlevel")).will(returnValue(item));
        expectActionPerformed(mockActionListener);
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
    }

    public void testAction() {
        assertEquals("Navigate", new NavigateEvent("one>two>three").action());
    }

    public void testTarget() {
        assertEquals("one>two>three", new NavigateEvent("one>two>three").target());
    }

    public void testParameters() {
        assertEquals("", new NavigateEvent("one>two>three").parameters());
    }

    public void testScriptLine() {
        assertEquals("Navigate one>two>three", new NavigateEvent("one>two>three").scriptLine());
    }

    private void expectActionPerformed(Mock mockActionListener) {
        mockActionListener.expects(once()).method("actionPerformed");
    }
}

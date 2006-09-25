package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.swing.*;

/**
 * Ensures behaviour of CheckBoxSelectEvent.
 */
public class SwitchTabEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        SwitchTabEvent eventOne = new SwitchTabEvent("parent.testTabName", "tabTwo");
        SwitchTabEvent eventTwo = new SwitchTabEvent("parent.testTabName", "tabTwo");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("SwitchTabEvent: parent.testTabName, tab: tabTwo", new SwitchTabEvent("parent.testTabName", "tabTwo").toString());
    }

    public void testAction() {
        assertEquals("SwitchTab", new SwitchTabEvent("parent.testTabName", "tabTwo").action());
    }

    public void testTarget() {
        assertEquals("parent.testTabName", new SwitchTabEvent("parent.testTabName", "tabTwo").target());
    }

    public void testParameters() {
        assertEquals("tabTwo", new SwitchTabEvent("parent.testTabName", "tabTwo").parameters());
    }

    public void testScriptLine() {
        assertEquals("switch_tab \"parent.testTabName\" \"tabTwo\"", new SwitchTabEvent("parent.testTabName", "tabTwo").scriptLine());
    }

    public void testPlay() {
        SwitchTabEvent event = new SwitchTabEvent("parent.testTabName", "tabTwo");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        DefaultWindowContext context = new DefaultWindowContext();
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("tabOne", new JPanel());
        pane.addTab("tabTwo", new JPanel());
        assertEquals(0, pane.getSelectedIndex());
        mockComponentFinder.expects(once()).method("findComponent").with(eq(context), eq("parent.testTabName")).will(returnValue(pane));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(1, pane.getSelectedIndex());
    }

    protected FrankensteinEvent createEvent() {
        return new SwitchTabEvent("parent.testTabName", "tabTwo");
    }

    public void testPlayForTabThatIsNotPresent() {
        SwitchTabEvent event = new SwitchTabEvent("parent.testTabName", "tabTwo");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        DefaultWindowContext context = new DefaultWindowContext();
        JTabbedPane pane = new JTabbedPane();
        mockComponentFinder.expects(once()).method("findComponent").with(eq(context), eq("parent.testTabName")).will(returnValue(pane));
        try {
            event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }
}

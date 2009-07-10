package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

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
        assertEquals("switch_tab \"parent.testTabName\" , \"tabTwo\"", new SwitchTabEvent("parent.testTabName", "tabTwo").scriptLine());
    }

    public void testScriptLineInJava() {
        assertEquals("switchTab(\"parent.testTabName\" , \"tabTwo\")", new SwitchTabEvent("parent.testTabName", "tabTwo").scriptLine(new JavaScriptStrategy()));
    }

    public void testPlaysEvent() {
        SwitchTabEvent event = new SwitchTabEvent("parent.testTabName", "tabTwo");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("tabOne", new JPanel());
        pane.addTab("tabTwo", new JPanel());
        assertEquals(0, pane.getSelectedIndex());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.testTabName")).will(returnValue(pane));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(1, pane.getSelectedIndex());
    }

    public void testPlaysEventWhenThereAreMoreThanTwoTabs() {
        SwitchTabEvent event = new SwitchTabEvent("parent.testTabName", "tabThree");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("tabOne", new JPanel());
        pane.addTab("tabTwo", new JPanel());
        pane.addTab("tabThree", new JPanel());
        assertEquals(0, pane.getSelectedIndex());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.testTabName")).will(returnValue(pane));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(2, pane.getSelectedIndex());
    }

    public void testSwitchesTabWithRegularExpression() {
        SwitchTabEvent event = new SwitchTabEvent("parent.testTabName", "regex:.*Two");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("tabOne", new JPanel());
        pane.addTab("tabTwo", new JPanel());
        assertEquals(0, pane.getSelectedIndex());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.testTabName")).will(returnValue(pane));
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
        assertEquals(1, pane.getSelectedIndex());
    }

    protected FrankensteinEvent createEvent() {
        return new SwitchTabEvent("parent.testTabName", "tabTwo");
    }

    public void testPlayForTabThatIsNotPresent() {
        SwitchTabEvent event = new SwitchTabEvent("parent.testTabName", "tabFour");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("tabOne", new JPanel());
        pane.addTab("tabTwo", new JPanel());
        pane.addTab("tabThree", new JPanel());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("parent.testTabName")).will(returnValue(pane));
        try {
            event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }
}

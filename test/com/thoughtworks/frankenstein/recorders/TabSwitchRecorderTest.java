package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.events.SwitchTabEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the switch recorder.
 */
public class TabSwitchRecorderTest extends AbstractRecorderTestCase {
    private JTabbedPane tabbedPane;
    private TabSwitchRecorder recorder;
    private Mock mockComponentVisibility;

    protected void setUp() throws Exception {
        super.setUp();
        tabbedPane = new JTabbedPane();
        tabbedPane.setName("testTabName");
        tabbedPane.add("one", new JPanel());
        tabbedPane.add("two", new JPanel());
        mockComponentVisibility = mock(ComponentVisibility.class);
        recorder = new TabSwitchRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy(), (ComponentVisibility) mockComponentVisibility.proxy());
    }

    public void testAddsListenerToTabbedPaneWhenTabbedPaneIsShown() {
        int listenerCount = changeListenerCount();
        recorder.componentShown(tabbedPane);
        assertTrue(changeListenerCount() == listenerCount + 1);
    }

    private int changeListenerCount() {
        return tabbedPane.getChangeListeners().length;
    }

    public void testRemovesListenerWhenTabbedPaneIsHidden() {
        int listenerCount = changeListenerCount();
        recorder.componentShown(tabbedPane);
        recorder.componentHidden(tabbedPane);
        assertEquals(listenerCount,  changeListenerCount());
    }

    public void testSwitchingTabPostsSelectEvent() {
        mockRecorder.expects(once()).method("record").with(eq(new SwitchTabEvent("testTabName", "two")));
        mockComponentVisibility.expects(once()).method("isShowing").with(eq(tabbedPane)).will(returnValue(true));
        recorder.componentShown(tabbedPane);
        tabbedPane.setSelectedIndex(1);
    }
}

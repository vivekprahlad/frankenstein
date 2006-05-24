package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;

import com.thoughtworks.frankenstein.common.WaitForIdle;
import com.thoughtworks.frankenstein.events.NavigateEvent;

/**
 * Ensures behavior of the JMenu recorder.
 */
public class MenuNavigationRecorderTest extends AbstractRecorderTestCase {
    private MenuNavigationRecorder recorder;
    private JMenuItem menuItem;
    private JMenuBar menubar;
    private JMenu menu;


    protected void setUp() throws Exception {
        super.setUp();
        recorder = new MenuNavigationRecorder((Recorder) mockRecorder.proxy(), null);
        menu = new JMenu("top");
        menuItem = new JMenuItem("item");
        menu.add("item");
        menubar = new JMenuBar();
        menubar.add(menu);
    }

    public void testAddsListenerOnMenuItemResize() {
        int actionListenerCount = menuItem.getActionListeners().length;
        recorder.componentShown(menuItem);
        assertTrue(menuItem.getActionListeners().length > actionListenerCount);
    }

    public void testRemovesListenerOnMenuItemHide() {
        int actionListenerCount = menuItem.getActionListeners().length;
        recorder.componentShown(menuItem);
        recorder.componentHidden(menuItem);
        new WaitForIdle().waitForIdle();
        assertTrue(menuItem.getActionListeners().length == actionListenerCount);
    }

    public void testAddsMenuChangeListenerAfterRegistration() {
        int listenerCount = menuChangeListenerCount();
        recorder.register();
        assertEquals(menuChangeListenerCount(), listenerCount + 1);
    }

    public void testRemovesMenuChangeListenerAfterUnRegistration() {
        int listenerCount = menuChangeListenerCount();
        recorder.register();
        recorder.unregister();
        assertEquals(menuChangeListenerCount(), listenerCount);
    }

    public void testRecordsNavigationEventWhenMenuItemIsClicked() {
        recorder.register();
        MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[] {menubar, menu, menuItem});
        recorder.componentShown(menuItem);
        mockRecorder.expects(once()).method("record").with(eq(new NavigateEvent("top>item")));
        menuItem.doClick();
    }

    private int menuChangeListenerCount() {
        return MenuSelectionManager.defaultManager().getChangeListeners().length;
    }
}

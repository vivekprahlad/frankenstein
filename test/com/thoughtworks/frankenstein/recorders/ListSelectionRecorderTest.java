package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.common.DefaultComponentDecoder;
import com.thoughtworks.frankenstein.events.SelectListEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures the behavior of the select recorder
 */
public class ListSelectionRecorderTest extends AbstractRecorderTestCase {
    private JList list;
    private ListSelectionRecorder recorder;
    private Mock mockComponentVisibility;

    protected void setUp() throws Exception {
        super.setUp();
        list = new JList();
        list.setName("testList");
        mockComponentVisibility = mock(ComponentVisibility.class);
        recorder = new ListSelectionRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy(), new DefaultComponentDecoder(), (ComponentVisibility) mockComponentVisibility.proxy());
    }

    public void testAddsComboBoxListenerWhenComboBoxIsShown() {
        int initialListenerCount = numberOfListeners();
        recorder.componentShown(list);
        assertTrue(numberOfListeners() == initialListenerCount + 1);
    }

    public void testRemovesComboBoxListenerWhenComboBoxIsHidden() {
        int initialListenerCount = numberOfListeners();
        recorder.componentShown(list);
        recorder.componentHidden(list);
        assertTrue(numberOfListeners() == initialListenerCount);
    }

    public void testSelectingChoiceRecordsEvent() {
        list.setModel(new DefaultComboBoxModel(new String[]{"abc", "def"}));
        mockRecorder.expects(once()).method("record").with(eq(new SelectListEvent("testList","def")));
        mockComponentVisibility.expects(once()).method("isShowing").with(eq(list)).will(returnValue(true));
        recorder.componentShown(list);
        list.setSelectedIndex(1);
    }

    public void testDoesNotRecordJListsThatBelongToFileChoosers() {
        list.setModel(new DefaultComboBoxModel(new String[]{"abc", "def"}));
        JFileChooser chooser = new JFileChooser(".");
        chooser.add(list);
        mockRecorder.expects(never()).method("record").with(ANYTHING);
        mockComponentVisibility.expects(once()).method("isShowing").with(eq(list)).will(returnValue(true));
        recorder.componentShown(list);
        list.setSelectedIndex(1);
    }

    private int numberOfListeners() {
        return list.getListSelectionListeners().length;
    }
}

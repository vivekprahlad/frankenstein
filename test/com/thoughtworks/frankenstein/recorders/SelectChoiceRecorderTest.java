package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.common.DefaultRendererDecoder;
import com.thoughtworks.frankenstein.events.SelectDropDownEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures the behavior of the select recorder
 */
public class SelectChoiceRecorderTest extends AbstractRecorderTestCase {
    private JComboBox comboBox;
    private SelectDropDownRecorder recorder;
    private Mock mockComponentVisibility;

    protected void setUp() throws Exception {
        super.setUp();
        comboBox = new JComboBox();
        comboBox.setName("testComboBox");
        mockComponentVisibility = mock(ComponentVisibility.class);
        recorder = new SelectDropDownRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy(), new DefaultRendererDecoder(), (ComponentVisibility) mockComponentVisibility.proxy());
    }

    public void testAddsComboBoxListenerWhenComboBoxIsShown() {
        int initialListenerCount = numberOfListeners();
        recorder.componentShown(comboBox);
        assertTrue(numberOfListeners() == initialListenerCount + 1);
    }

    public void testRemovesComboBoxListenerWhenComboBoxIsHidden() {
        int initialListenerCount = numberOfListeners();
        recorder.componentShown(comboBox);
        recorder.componentHidden(comboBox);
        assertTrue(numberOfListeners() == initialListenerCount);
    }

    public void testSelectingChoiceRecordsEvent() {
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"abc", "def"}));
        mockRecorder.expects(once()).method("record").with(eq(new SelectDropDownEvent("testComboBox", "def")));
        mockComponentVisibility.expects(once()).method("isShowingAndHasFocus").with(eq(comboBox)).will(returnValue(true));
        recorder.componentShown(comboBox);
        comboBox.setSelectedItem("def");
    }

    private int numberOfListeners() {
        return comboBox.getItemListeners().length;
    }

}

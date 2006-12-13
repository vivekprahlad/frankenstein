package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;

import com.thoughtworks.frankenstein.events.CheckboxEvent;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the check recorder
 */
public class CheckBoxRecorderTest extends AbstractRecorderTestCase {
    private JCheckBox checkBox;
    private CheckBoxRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        checkBox = new JCheckBox();
        checkBox.setName("testCheckBox");
        checkBox.setText("testCheckBox");
        mockRecorder = mock(Recorder.class);
        recorder = new CheckBoxRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsActionListenerWhenCheckBoxIsShown() {
        int initialActionListenerCount = numberOfActionListeners(checkBox);
        recorder.componentShown(checkBox);
        assertEquals(initialActionListenerCount+1, numberOfActionListeners(checkBox));

    }

    public void testRemovesActionListenerWhenCheckBoxIsHidden() {
        int initialActionListenerCount = numberOfActionListeners(checkBox);
        recorder.componentShown(checkBox);
        recorder.componentHidden(checkBox);
        assertEquals(initialActionListenerCount, numberOfActionListeners(checkBox));
    }

    public void testRecordsEventWhenCheckBoxIsSelected() {
        checkBox.setSelected(true);
        recorder.componentShown(checkBox);
        mockRecorder.expects(once()).method("record").with(eq(new CheckboxEvent("testCheckBox", false, new ClickAction())));
        checkBox.doClick();
    }

    private int numberOfActionListeners(JCheckBox checkBox) {
        return checkBox.getActionListeners().length;
    }
}

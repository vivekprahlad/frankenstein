package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;

import com.thoughtworks.frankenstein.events.RadioButtonEvent;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the check recorder
 */
public class RadioButtonRecorderTest extends AbstractRecorderTestCase {
    private JRadioButton radioButton;
    private RadiobuttonRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        radioButton = new JRadioButton();
        radioButton.setName("testCheckBox");
        radioButton.setText("testCheckBox");
        mockRecorder = mock(Recorder.class);
        recorder = new RadiobuttonRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsActionListenerWhenCheckBoxIsShown() {
        int initialActionListenerCount = numberOfActionListeners(radioButton);
        recorder.componentShown(radioButton);
        assertEquals(initialActionListenerCount + 1, numberOfActionListeners(radioButton));

    }

    public void testRemovesActionListenerWhenCheckBoxIsHidden() {
        int initialActionListenerCount = numberOfActionListeners(radioButton);
        recorder.componentShown(radioButton);
        recorder.componentHidden(radioButton);
        assertEquals(initialActionListenerCount, numberOfActionListeners(radioButton));
    }

    public void testRecordsEventWhenCheckBoxIsSelected() {
        radioButton.setSelected(true);
        recorder.componentShown(radioButton);
        mockRecorder.expects(once()).method("record").with(eq(new RadioButtonEvent("testCheckBox", new ClickAction())));
        radioButton.doClick();
    }

    private int numberOfActionListeners(JRadioButton checkBox) {
        return checkBox.getActionListeners().length;
    }
}

package com.thoughtworks.frankenstein.recorders;

import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxButton;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;
import com.thoughtworks.frankenstein.events.ClickButtonEvent;
import com.thoughtworks.frankenstein.naming.ComponentHierarchyWalker;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.naming.UnnamedComponentMatchingRule;
import com.thoughtworks.frankenstein.ui.RecorderPane;

/**
 * Ensures behaviour of the ButtonClickRecorder
 */
public class ButtonClickRecorderTest extends AbstractRecorderTestCase {
    private JButton button;
    private ButtonClickRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        button = new JButton("abc");
        new JFrame().getContentPane().add(button);
        recorder = new ButtonClickRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsActionListenerWhenButtonIsShown() {
        int listenerCount = listenerCount();
        recorder.componentShown(button);
        assertTrue(listenerCount() == listenerCount + 1);
    }

    public void testRemovesActionListenerWhenButtonIsHidden() {
        int listenerCount = listenerCount();
        recorder.componentShown(button);
        recorder.componentHidden(button);
        assertTrue(listenerCount() == listenerCount);
    }

    public void testRecordsEventOnForButtonsWithText() {
        recorder.componentShown(button);
        mockRecorder.expects(once()).method("record").with(eq(new ClickButtonEvent("abc")));
        button.doClick();
    }

    public void testRecordsEventsForButtonsWithImages() {
        button = new JButton(new ImageIcon("icons/list-add.png"));
        new JFrame().getContentPane().add(button);
        recorder.componentShown(button);
        mockRecorder.expects(once()).method("record").with(eq(new ClickButtonEvent("icons/list-add.png")));
        button.doClick();
    }

    public void testRecordsToggleButtons() {
        JToggleButton button = new JToggleButton("abc");
        new JFrame().getContentPane().add(button);
        recorder.componentShown(button);
        mockRecorder.expects(once()).method("record").with(eq(new ClickButtonEvent("abc")));
        button.doClick();
    }

    public void testDoesNotRecordRadioButtons() {
        JRadioButton button = new JRadioButton("abc");
        assertFalse(recorder.matchesComponentType(new ComponentEvent(button, ComponentEvent.COMPONENT_SHOWN)));
    }

    public void testDoesNotRecordJMenuItems() {
        JMenuItem button = new JMenuItem("abc");
        assertFalse(recorder.matchesComponentType(new ComponentEvent(button, ComponentEvent.COMPONENT_SHOWN)));
    }

    public void testDoesNotRecordOtherComponents() {
        JTextField field = new JTextField();
        assertFalse(recorder.matchesComponentType(new ComponentEvent(field, ComponentEvent.COMPONENT_SHOWN)));
    }

    public void testDoesNotRecordComboBoxButtons() {
        JButton comboButton = new MetalComboBoxButton(new JComboBox(), null, null, null);
        assertFalse(recorder.matchesComponentType(new ComponentEvent(comboButton, ComponentEvent.COMPONENT_SHOWN)));
    }

    public void testDoesNotRecordRecorderPaneButtons() {
        Mock mockFrankensteinRecorder = mock(FrankensteinRecorder.class);
        mockFrankensteinRecorder.expects(once()).method("start");
        RecorderPane pane = new RecorderPane((FrankensteinRecorder) mockFrankensteinRecorder.proxy(), null, null);
        new JFrame().getContentPane().add(pane);
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JButton.class);
        new ComponentHierarchyWalker().matchComponentsIn(pane, rule);
        assertFalse(rule.hasNoMatches());
        JButton startButton = (JButton) rule.unnamedComponents().get(0);
        recorder.componentShown(startButton);
        startButton.doClick();
    }

    private int listenerCount() {
        return button.getActionListeners().length;
    }
}

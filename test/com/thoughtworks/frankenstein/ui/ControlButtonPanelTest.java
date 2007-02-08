package com.thoughtworks.frankenstein.ui;

import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;
import com.thoughtworks.frankenstein.naming.ComponentHierarchyWalker;
import com.thoughtworks.frankenstein.naming.UnnamedComponentMatchingRule;


public class ControlButtonPanelTest extends MockObjectTestCase {
    private Mock mockFrankensteinRecorder;
    private Mock mockFileDialogLauncher;
    private ControlButtonPanel panel;
    private JFrame frame;


    protected void setUp() throws Exception {
        super.setUp();
        mockFrankensteinRecorder = mock(FrankensteinRecorder.class);
        mockFileDialogLauncher = mock(FileDialogLauncher.class);
        panel = new ControlButtonPanel((FrankensteinRecorder) mockFrankensteinRecorder.proxy(),
                (FileDialogLauncher) mockFileDialogLauncher.proxy());
        frame = new JFrame();
        frame.getContentPane().add(panel);
    }

    public void testAsksRecorderToRecorderWhenRecordButtonIsClicked() {
        JButton button = button(0);
        assertEquals("Record", button.getText());
        mockFrankensteinRecorder.expects(once()).method("start");
        button.doClick();
    }

    public void testAsksRecorderToStopWhenStopButtonIsClicked() {
        JButton button = button(1);
        assertEquals("Stop", button.getText());
        mockFrankensteinRecorder.expects(once()).method("stop");
        button.doClick();
    }

    public void testAsksRecorderToPlayWhenPlayButtonIsClicked() {
        JButton button = button(2);
        assertEquals("Play", button.getText());
        mockFrankensteinRecorder.expects(once()).method("play");
        button.doClick();
    }

    public void testAsksRecorderToResetWhenResetButtonIsClicked() {
        JButton button = button(5);
        assertEquals("Reset", button.getText());
        mockFrankensteinRecorder.expects(once()).method("reset");
        button.doClick();
    }

    public void testAsksRecorderToSaveWhenSaveButtonIsClicked() {
        JButton button = button(3);
        assertEquals("Save", button.getText());
        mockFrankensteinRecorder.expects(once()).method("stop");
        mockFileDialogLauncher.expects(once())
                .method("launchSaveDialog")
                .with(eq(panel), isA(JFileChooser.class), isA(FrankensteinRecorder.class));
        button.doClick();
    }

    public void testAsksRecorderToLoadScriptWhenLoadButtonIsClicked() {
        JButton button = button(4);
        assertEquals("Load", button.getText());
        mockFrankensteinRecorder.expects(once()).method("stop");
        mockFileDialogLauncher.expects(once())
                .method("launchOpenDialog")
                .with(eq(panel), isA(JFileChooser.class), isA(FrankensteinRecorder.class));
        button.doClick();
    }

    private JButton button(int index) {
        return (JButton) getComponent(index, JButton.class);
    }

    private JComponent getComponent(int index, Class componentType) {
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(componentType);
        new ComponentHierarchyWalker().matchComponentsIn(frame, rule);
        return (JComponent) rule.unnamedComponents().get(index);
    }
}

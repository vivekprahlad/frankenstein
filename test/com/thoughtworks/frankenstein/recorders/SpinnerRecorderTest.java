package com.thoughtworks.frankenstein.recorders;


import junit.framework.TestCase;

import javax.swing.*;

public class SpinnerRecorderTest extends TestCase {

    public void testClearsDefaultEditorName() {
        JSpinner spinner = new JSpinner();
        new SpinnerRecorder(null, null).componentShown(spinner);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        assertEquals(null, editor.getTextField().getName());
    }

    public void testClearsArbitraryEditorName() {
        JSpinner spinner = new JSpinner();
        JTextField textFieldEditor = new JTextField();
        textFieldEditor.setName("spinner");
        spinner.setEditor(textFieldEditor);
        new SpinnerRecorder(null, null).componentShown(spinner);
        assertEquals(null, textFieldEditor.getName());
    }
}


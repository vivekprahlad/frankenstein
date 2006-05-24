package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thoughtworks.frankenstein.events.EnterTextEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording text entered in text fields.
 * @author Vivek Prahlad
 */
public class TextFieldRecorder extends AbstractComponentRecorder {
    private Map listenerMap = new HashMap();
    private ComponentVisibility visibility;

    public TextFieldRecorder(EventRecorder recorder, NamingStrategy namingStrategy, ComponentVisibility visibility) {
        super(recorder, namingStrategy, JTextField.class);
        this.visibility = visibility;
    }

    void componentShown(Component component) {
        DocumentListener listener = new TextFieldListener(textField(component));
        textField(component).getDocument().addDocumentListener(listener);
        listenerMap.put(textField(component), listener);
    }

    void componentHidden(Component component) {
        DocumentListener listener = (DocumentListener) listenerMap.remove(textField(component));
        textField(component).getDocument().removeDocumentListener(listener);
    }

    private JTextField textField(Component component) {
        return (JTextField) component;
    }

    private class TextFieldListener implements DocumentListener {
        private JTextField textField;

        public TextFieldListener(JTextField textField) {
            this.textField = textField;
        }

        public void insertUpdate(DocumentEvent e) {
            record();
        }

        private void record() {
            if (!visibility.isShowingAndHasFocus(textField)) return;
            recorder.record(new EnterTextEvent(componentName(textField), textField.getText()));
        }

        public void removeUpdate(DocumentEvent e) {
            record();
        }

        public void changedUpdate(DocumentEvent e) {
            //Do nothing for default text fields.
        }
    }
}

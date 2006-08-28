package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.events.SelectFileEvent;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Records file choices
 */
public class FileChooserRecorder extends AbstractComponentRecorder implements PropertyChangeListener {

    public FileChooserRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JFileChooser.class);
    }

    void componentShown(Component component) {
        filechooser(component).addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, this);
    }

    private JFileChooser filechooser(Component component) {
        return (JFileChooser) component;
    }

    void componentHidden(Component component) {
        filechooser(component).removePropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue()!=null)
            recorder.record(new SelectFileEvent(evt.getNewValue().toString()));
    }
}

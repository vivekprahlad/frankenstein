package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.SelectFileEvent;
import com.thoughtworks.frankenstein.events.SelectFilesEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Records file choices
 */
public class FileChooserRecorder extends AbstractComponentRecorder {
    private SelectedFilePropertyChangeListener selectedFileListener;
    private SelectedFilesPropertyChangeListener selectedFilesListener;

    public FileChooserRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JFileChooser.class);
        selectedFileListener = new SelectedFilePropertyChangeListener();
        selectedFilesListener = new SelectedFilesPropertyChangeListener();
    }

    void componentShown(Component component) {
        filechooser(component).addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, selectedFileListener);
        filechooser(component).addPropertyChangeListener(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY, selectedFilesListener);
    }

    private JFileChooser filechooser(Component component) {
        return (JFileChooser) component;
    }

    void componentHidden(Component component) {
        filechooser(component).removePropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, selectedFileListener);
        filechooser(component).removePropertyChangeListener(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY, selectedFilesListener);
    }

    private class SelectedFilePropertyChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue()!=null)
                recorder.record(new SelectFileEvent(evt.getNewValue().toString()));
        }
    }

    private class SelectedFilesPropertyChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue()!=null) {
                recorder.record(new SelectFilesEvent(fileNames((File[]) evt.getNewValue())));
            }

        }

        private String fileNames(File[] files) {
            String fileList = "";
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                fileList += file.toString() + ",";
            }
            return  fileList.substring(0, fileList.length()-1);
        }
    }
}

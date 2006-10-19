package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.events.SelectFileEvent;

import javax.swing.*;
import java.io.File;

/**
 * Ensures behaviour of SelectFileRecorder
 */
public class FileChooserRecorderTest extends AbstractRecorderTestCase {

    public void testAddsPropertyChangeListenerWhenComponentIsShown() {
        FileChooserRecorder recorder = new FileChooserRecorder((EventRecorder) mockRecorder.proxy(), null);
        JFileChooser chooser = new JFileChooser(".");
        int initialListenerCount = listenerCount(chooser);
        recorder.componentShown(chooser);
        assertEquals(initialListenerCount + 2, listenerCount(chooser));
    }

    public void testRemovesPropertyChangeListenerWhenComponentIsHidden() {
        FileChooserRecorder recorder = new FileChooserRecorder((EventRecorder) mockRecorder.proxy(), null);
        JFileChooser chooser = new JFileChooser(".");
        int initialListenerCount = listenerCount(chooser);
        recorder.componentShown(chooser);
        recorder.componentHidden(chooser);
        assertEquals(listenerCount(chooser), initialListenerCount);
    }

    public void testRecordsSelectedFileWhenSelectionChanges() {
        FileChooserRecorder recorder = new FileChooserRecorder((EventRecorder) mockRecorder.proxy(), null);
        JFileChooser chooser = new JFileChooser(".");
        int initialListenerCount = listenerCount(chooser);
        recorder.componentShown(chooser);
        mockRecorder.expects(once())
                .method("record")
                .with(eq(new SelectFileEvent("com/thoughtworks/frankenstein/recorders/FileChooserRecorderTest.java")));
        chooser.setSelectedFile(new File("com/thoughtworks/frankenstein/recorders/FileChooserRecorderTest.java"));
        recorder.componentHidden(chooser);
    }

    private int listenerCount(JFileChooser chooser) {
        return chooser.getPropertyChangeListeners().length;
    }
}

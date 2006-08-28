package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Ensures behaviour of SelectFileEvent
 */
public class SelectFileEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        SelectFileEvent one = new SelectFileEvent("/home/test/file");
        SelectFileEvent two = new SelectFileEvent("/home/test/file");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("SelectFileEvent: /home/test/file", new SelectFileEvent("/home/test/file").toString());
    }

    public void testAction() {
        assertEquals("SelectFile", new SelectFileEvent("/home/test/file").action());
    }

    public void testTarget() {
        assertEquals("/home/test/file", new SelectFileEvent("/home/test/file").target());
    }

    public void testParameters() {
        assertEquals("", new SelectFileEvent("/home/test/file").parameters());
    }

    public void testScriptLine() {
        assertEquals("select_file \"/home/test/file\"", new SelectFileEvent("/home/test/file").scriptLine());
    }

    public void testPlay() throws Exception {
        JFileChooser chooser = new JFileChooser(".");
        Mock mockFinder = mock(ComponentFinder.class);
        mockFinder.expects(once()).method("fileChooser").will(returnValue(chooser));
        new SelectFileEvent("com/thoughtworks/frankenstein/events/SelectFileEvent.java").play(null,
                (ComponentFinder) mockFinder.proxy(), null, null);
        assertEquals("com/thoughtworks/frankenstein/events/SelectFileEvent.java", chooser.getSelectedFile().getPath());
    }
}

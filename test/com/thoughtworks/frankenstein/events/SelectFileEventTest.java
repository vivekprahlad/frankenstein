package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Ensures behaviour of SelectFileEvent
 */
public class SelectFileEventTest extends AbstractEventTestCase {

      public void testEqualsAndHashCode() {
        SelectFileEvent one = new SelectFileEvent(file("/home/test/file"));
        SelectFileEvent two = new SelectFileEvent(file("/home/test/file"));
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("SelectFileEvent:"+file(" /home/test/file"), new SelectFileEvent(file("/home/test/file")).toString());
    }

    public void testAction() {
        assertEquals("SelectFile", new SelectFileEvent(file("/home/test/file")).action());
    }

    public void testTarget() {
        assertEquals(file("/home/test/file"), new SelectFileEvent(file("/home/test/file")).target());
    }

    public void testParameters() {
        assertEquals("", new SelectFileEvent("/home/test/file").parameters());
    }

    public void testScriptLine() {
        assertEquals("select_file \""+file("/home/test/file")+"\"", new SelectFileEvent(file("/home/test/file")).scriptLine());
    }

    public void testDoesNotReplaceSInScriptLine() {
        assertEquals("select_file \"" + file("/home/sabc/file") + "\"", new SelectFileEvent(file("/home/sabc/file")).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JFileChooser chooser = new JFileChooser(".");
        Mock mockFinder = mock(ComponentFinder.class);
        mockFinder.expects(once()).method("findFileChooser").will(returnValue(chooser));
        new SelectFileEvent(file("com/thoughtworks/frankenstein/events/SelectFileEvent.java")).play(null,
                (ComponentFinder) mockFinder.proxy(), null, null);
        assertEquals(file("com/thoughtworks/frankenstein/events/SelectFileEvent.java"), chooser.getSelectedFile().getPath());
    }

    protected FrankensteinEvent createEvent() {
        return new SelectFileEvent(file("com/thoughtworks/frankenstein/events/SelectFileEvent.java"));
    }
}

package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Ensures behaviour of SelectFileEvent
 */
public class SelectFilesEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        SelectFilesEvent one = new SelectFilesEvent(file("/home/test/file") + "," + file("/home/test/file2"));
        SelectFilesEvent two = new SelectFilesEvent(file("/home/test/file") + "," + file("/home/test/file2"));
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("SelectFilesEvent: " + file("/home/test/file") + "," + file("/home/test/file2"), new SelectFilesEvent(file("/home/test/file") + "," + file("/home/test/file2")).toString());
    }

    public void testAction() {
        assertEquals("SelectFiles", new SelectFilesEvent(file("/home/test/file") + "," + file("/home/test/file2")).action());
    }

    public void testTarget() {
        assertEquals(file("/home/test/file") + "," + file("/home/test/file2"), new SelectFilesEvent(file("/home/test/file") + "," + file("/home/test/file2")).target());
    }

    public void testParameters() {
        assertEquals("", new SelectFilesEvent(file("/home/test/file") + "," + file("/home/test/file2")).parameters());
    }

    public void testScriptLine() {
        assertEquals("select_files \"" + file("/home/test/file") + "," + file("/home/test/file2") + "\"", new SelectFilesEvent(file("/home/test/file") + "," + file("/home/test/file2")).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JFileChooser chooser = new JFileChooser(".");
        Mock mockFinder = mock(ComponentFinder.class);
        mockFinder.expects(once()).method("findFileChooser").will(returnValue(chooser));
        new SelectFilesEvent(file("path/one") + "," + file("path/two")).play(null,
                (ComponentFinder) mockFinder.proxy(), null, null);
        assertEquals(file("path/one"), chooser.getSelectedFiles()[0].getPath());
        assertEquals(file("path/two"), chooser.getSelectedFiles()[1].getPath());
    }

    public void testReplacesSelectFileEvent() {
        SelectFileEvent selectFileEvent = new SelectFileEvent(file("/path/one"));
        SelectFilesEvent filesEvent = new SelectFilesEvent(file("/path/one") + "," + file("path/two"));
        Mock eventList = mock(EventList.class);
        eventList.expects(once()).method("replaceLastEvent").with(same(filesEvent));
        filesEvent.record((EventList) eventList.proxy(), selectFileEvent);
    }

    public void testReplacesSelectFilesEvents() {
        SelectFilesEvent previousEvent = new SelectFilesEvent(file("/path/one") + "," + file("path/two"));
        SelectFilesEvent filesEvent = new SelectFilesEvent(file("/path/one") + "," + file("path/two") + "," + file("path/three"));
        Mock eventList = mock(EventList.class);
        eventList.expects(once()).method("replaceLastEvent").with(same(filesEvent));
        filesEvent.record((EventList) eventList.proxy(), previousEvent);
    }

    public void testAddsSelectFilesEventWhenThereAreNoPreviousEvents() {
        SelectFilesEvent filesEvent = new SelectFilesEvent(file("/path/one") + "," + file("path/two"));
        Mock eventList = mock(EventList.class);
        eventList.expects(once()).method("addEvent").with(same(filesEvent));
        filesEvent.record((EventList) eventList.proxy(), null);
    }

    public void testIfArrayOfStringObjectsAreJoinedCorrectly() {
        String[] filePaths = new String[]{"/home/test/file1", "/home/test/file2"};
        SelectFilesEvent event = new SelectFilesEvent(filePaths);

        assertEquals("/home/test/file1,/home/test/file2", event.target());
    }

    protected FrankensteinEvent createEvent() {
        return new SelectFilesEvent(file("path/one") + "," + file("path/two"));
    }
}

package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Ensures behaviour of SelectFileEvent
 */
public class SelectFilesEventTest extends AbstractEventTestCase {
    public void testEqualsAndHashCode() {
        SelectFilesEvent one = new SelectFilesEvent("/home/test/file,/home/test/file2");
        SelectFilesEvent two = new SelectFilesEvent("/home/test/file,/home/test/file2");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("SelectFilesEvent: /home/test/file,/home/test/file2", new SelectFilesEvent("/home/test/file,/home/test/file2").toString());
    }

    public void testAction() {
        assertEquals("SelectFiles", new SelectFilesEvent("/home/test/file,/home/test/file2").action());
    }

    public void testTarget() {
        assertEquals("/home/test/file,/home/test/file2", new SelectFilesEvent("/home/test/file,/home/test/file2").target());
    }

    public void testParameters() {
        assertEquals("", new SelectFilesEvent("/home/test/file,/home/test/file2").parameters());
    }

    public void testScriptLine() {
        assertEquals("select_files \"/home/test/file,/home/test/file2\"", new SelectFilesEvent("/home/test/file,/home/test/file2").scriptLine());
    }

    public void testPlay() throws Exception {
        JFileChooser chooser = new JFileChooser(".");
        Mock mockFinder = mock(ComponentFinder.class);
        mockFinder.expects(once()).method("findFileChooser").will(returnValue(chooser));
        new SelectFilesEvent("path/one,path/two").play(null,
                (ComponentFinder) mockFinder.proxy(), null, null);
        assertEquals("path/one", chooser.getSelectedFiles()[0].getPath());
        assertEquals("path/two", chooser.getSelectedFiles()[1].getPath());
    }

    public void testReplacesSelectFileEvent() {
        SelectFileEvent selectFileEvent = new SelectFileEvent("/path/one");
        SelectFilesEvent filesEvent = new SelectFilesEvent("/path/one,path/two");
        Mock eventList = mock(EventList.class);
        eventList.expects(once()).method("replaceLastEvent").with(same(filesEvent));
        filesEvent.record((EventList) eventList.proxy(), selectFileEvent);
    }

    public void testReplacesSelectFilesEvents() {
        SelectFilesEvent previousEvent = new SelectFilesEvent("/path/one,path/two");
        SelectFilesEvent filesEvent = new SelectFilesEvent("/path/one,path/two,path/three");
        Mock eventList = mock(EventList.class);
        eventList.expects(once()).method("replaceLastEvent").with(same(filesEvent));
        filesEvent.record((EventList) eventList.proxy(), previousEvent);
    }

    protected FrankensteinEvent createEvent() {
        return new SelectFilesEvent("path/one,path/two");
    }
}

package com.thoughtworks.frankenstein.application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.common.DefaultRendererDecoder;
import com.thoughtworks.frankenstein.events.WindowActivatedEvent;
import com.thoughtworks.frankenstein.recorders.DefaultComponentVisibility;
import com.thoughtworks.frankenstein.recorders.Recorder;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of composite recorder.
 */
public class DefaultFrankensteinRecorderTest extends MockObjectTestCase {
    private Mock mockRecorder;
    private DefaultFrankensteinRecorder compositeRecorder;

    protected void setUp() throws Exception {
        super.setUp();
        mockRecorder = mock(Recorder.class);
        mockRecorder.expects(once()).method("stop");
        compositeRecorder = new DefaultFrankensteinRecorder((Recorder) mockRecorder.proxy(), new DefaultRendererDecoder(),
                new DefaultComponentVisibility(), new DefaultNamingStrategy());
    }

    public void testReset() {
        mockRecorder.expects(once()).method("reset");
        compositeRecorder.reset();
    }

    public void testStop() {
        mockRecorder.expects(once()).method("stop");
        compositeRecorder.stop();
    }

    public void testStart() {
        mockRecorder.expects(once()).method("start");
        compositeRecorder.start();
    }

    public void testPlay() {
        mockRecorder.expects(once()).method("play");
        compositeRecorder.play();
    }

    public void testSave() throws IOException {
        mockRecorder.expects(once()).method("eventList").will(returnValue(eventList()));
        File file = new File("testscript");
        compositeRecorder.save(file);
        assertTrue(file.exists());
        file.delete();
    }

    public void testLoad() throws IOException {
        mockRecorder.expects(once()).method("eventList").will(returnValue(eventList()));
        File file = new File("testscript");
        compositeRecorder.save(file);
        mockRecorder.expects(once()).method("setEventList").with(eq(eventList()));
        compositeRecorder.load(new FileReader(file));
        file.delete();
    }

    private List eventList() {
        List eventList = new ArrayList();
        eventList.add(new WindowActivatedEvent("title"));
        return eventList;
    }
}

package com.thoughtworks.frankenstein.events;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.WindowContext;

import javax.swing.*;

/**
 * Ensures behaviour of DialogShownEvent
 */
public class InternalFrameShownEventTest extends MockObjectTestCase {

    public void testEqualsAndHashCode() {
        InternalFrameShownEvent eventOne = new InternalFrameShownEvent("title");
        InternalFrameShownEvent eventTwo = new InternalFrameShownEvent("title");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("InternalFrameShownEvent: title", new InternalFrameShownEvent("title").toString());
    }

    public void testAction() {
        assertEquals("InternalFrameShown", new InternalFrameShownEvent("testFrame").action());
    }

    public void testTarget() {
        assertEquals("testFrame", new InternalFrameShownEvent("testFrame").target());
    }

    public void testParameters() {
        assertEquals("", new InternalFrameShownEvent("testFrame").parameters());
    }

    public void testScriptLine() {
        assertEquals("internal_frame_shown \"testFrame\"", new InternalFrameShownEvent("testFrame").scriptLine());
    }

    public void testNoErrorIfTitleMatches() {
        InternalFrameShownEvent event = new InternalFrameShownEvent("title");
        Mock mockWindownContext = mock(WindowContext.class);
        mockWindownContext.expects(once()).method("activeWindow").will(returnValue(new JInternalFrame("title")));
        event.play((WindowContext) mockWindownContext.proxy(), null, null, null);
    }

    public void testDoesNotProceedIfTitleDoesNotMatch() {
        InternalFrameShownEvent event = new InternalFrameShownEvent("wrongtitle");
        Mock mockWindownContext = mock(WindowContext.class);
        mockWindownContext.expects(once()).method("activeWindow").will(returnValue(new JDialog(new JFrame(), "title")));
        try {
            event.play((WindowContext) mockWindownContext.proxy(), null, null, null);
            fail();
        } catch (Exception e) {
        }
    }
}

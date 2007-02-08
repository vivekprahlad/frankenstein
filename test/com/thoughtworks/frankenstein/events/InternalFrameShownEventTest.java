package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.playback.ComponentFinder;

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
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(new JInternalFrame("title")));
        event.play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
    }

    public void testDoesNotProceedIfTitleDoesNotMatch() {
        InternalFrameShownEvent event = new InternalFrameShownEvent("wrongtitle");
        Mock mockComponentFinder = mock(ComponentFinder.class);
        mockComponentFinder.expects(once()).method("findInternalFrame").will(returnValue(null));
        try {
            event.play(null, (ComponentFinder) mockComponentFinder.proxy(), null, null);
            fail();
        } catch (Exception e) {
        }
    }
}

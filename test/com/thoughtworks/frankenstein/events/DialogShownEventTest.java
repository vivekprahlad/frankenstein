package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.WindowContext;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * Ensures behaviour of DialogShownEvent
 */
public class DialogShownEventTest extends MockObjectTestCase {

    public void testEqualsAndHashCode() {
        DialogShownEvent eventOne = new DialogShownEvent("title");
        DialogShownEvent eventTwo = new DialogShownEvent("title");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("DialogShownEvent: title", new DialogShownEvent("title").toString());
    }

    public void testAction() {
        assertEquals("DialogShown", new DialogShownEvent("testDialog").action());
    }

    public void testTarget() {
        assertEquals("testDialog", new DialogShownEvent("testDialog").target());
    }

    public void testParameters() {
        assertEquals("", new DialogShownEvent("testDialog").parameters());
    }

    public void testScriptLine() {
        assertEquals("DialogShown testDialog", new DialogShownEvent("testDialog").scriptLine());
    }

    public void testNoErrorIfTitleMatches() {
        DialogShownEvent event = new DialogShownEvent("title");
        Mock mockWindownContext = mock(WindowContext.class);
        mockWindownContext.expects(once()).method("waitForDialog").with(eq("title"), eq(10));
        event.play((WindowContext) mockWindownContext.proxy(), null, null, null);
    }

    public void testInterruptedException() {
        DialogShownEvent event = new DialogShownEvent("title");
        Mock mockWindownContext = mock(WindowContext.class);
        mockWindownContext.expects(once()).method("waitForDialog").with(eq("title"), eq(10)).will(throwException(new InterruptedException()));
        try {
            event.play((WindowContext) mockWindownContext.proxy(), null, null, null);
            fail();
        } catch (Exception e) {
        }
    }
}
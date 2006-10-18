package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.WindowContext;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * Ensures behaviour of DialogShownEvent
 */
public class DialogShownEventTest extends MockObjectTestCase {

    public void testEqualsAndHashCode() {
        DialogShownEvent eventOne = new DialogShownEvent("2");
        DialogShownEvent eventTwo = new DialogShownEvent("2");
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(), eventTwo.hashCode());
    }

    public void testToString() {
        assertEquals("DialogShownEvent: 10", new DialogShownEvent("10").toString());
    }

    public void testAction() {
        assertEquals("DialogShown", new DialogShownEvent("2").action());
    }

    public void testTarget() {
        assertEquals("2", new DialogShownEvent("2").target());
    }

    public void testParameters() {
        assertEquals("", new DialogShownEvent("2").parameters());
    }

    public void testScriptLine() {
        assertEquals("dialog_shown \"3\"", new DialogShownEvent("3").scriptLine());
    }

    public void testNoErrorIfTitleMatches() {
        DialogShownEvent event = new DialogShownEvent("2");
        Mock mockWindownContext = mock(WindowContext.class);
        mockWindownContext.expects(once()).method("waitForDialog").with( eq(2));
        event.play((WindowContext) mockWindownContext.proxy(), null, null, null);
    }

    public void testInterruptedException() {
        DialogShownEvent event = new DialogShownEvent("10");
        Mock mockWindownContext = mock(WindowContext.class);
        mockWindownContext.expects(once()).method("waitForDialog").with(eq(10)).will(throwException(new InterruptedException()));
        try {
            event.play((WindowContext) mockWindownContext.proxy(), null, null, null);
            fail();
        } catch (Exception e) {
        }
    }
}
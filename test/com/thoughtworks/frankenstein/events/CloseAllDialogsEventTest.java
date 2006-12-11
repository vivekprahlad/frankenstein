package com.thoughtworks.frankenstein.events;

import org.jmock.Mock;
import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Ensures behaviour of CloseAllDialogsEvent
 * @author vivek
 */
public class CloseAllDialogsEventTest extends AbstractEventTestCase{
    public void testEqualsAndHashCode() {
        CloseAllDialogsEvent one = new CloseAllDialogsEvent("");
        CloseAllDialogsEvent two = new CloseAllDialogsEvent("");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testToString() {
        assertEquals("CloseAllDialogsEvent", new CloseAllDialogsEvent("").toString());
    }

    public void testAction() {
        assertEquals("CloseAllDialogs", new CloseAllDialogsEvent("").action());
    }

    public void testTarget() {
        assertEquals("", new CloseAllDialogsEvent("").target());
    }

    public void testParameters() {
        assertEquals("", new CloseAllDialogsEvent("").parameters());
    }

    public void testScriptLine() {
        assertEquals("close_all_dialogs", new CloseAllDialogsEvent("").scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        Mock mockWindowContext = mock(WindowContext.class);
        mockWindowContext.expects(once()).method("closeAllDialogs");
        new CloseAllDialogsEvent("").play((WindowContext) mockWindowContext.proxy(), null, null, null);
    }

    protected FrankensteinEvent createEvent() {
        return new CloseAllDialogsEvent("");
    }
}

package com.thoughtworks.frankenstein.events;

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

    public void testPlay() throws Exception {
    }

    protected FrankensteinEvent createEvent() {
        return new CloseAllDialogsEvent("");
    }
}

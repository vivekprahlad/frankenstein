package com.thoughtworks.frankenstein.events;

import java.io.File;

import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.common.WaitForIdle;

/**
 * Base test case for all events.
 */
public abstract class AbstractEventTestCase extends MockObjectTestCase {
    public abstract void testEqualsAndHashCode();

    public abstract void testToString();

    public abstract void testAction();

    public abstract void testTarget();

    public abstract void testParameters();

    public abstract void testScriptLine();

    public abstract void testPlaysEvent() throws Exception;

    public void testSavesAndRestoresEventFromScriptLine() throws Exception {
        FrankensteinEvent event = createEvent();
        assertEquals(event, createEventFromScriptLine(event.getClass(), event.scriptLine()));
    }

    private FrankensteinEvent createEventFromScriptLine(Class aClass, String scriptLine) throws Exception {
        DefaultEventRegistry eventRegistry = new DefaultEventRegistry();
        eventRegistry.registerEvent(aClass);
        return eventRegistry.createEvent(scriptLine);
    }

    protected abstract FrankensteinEvent createEvent();

    protected void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

    protected String file(String s) {
        return s.replaceAll("/", (File.separator.equals("/")) ? File.separator : ("\\" + File.separator));
    }
}

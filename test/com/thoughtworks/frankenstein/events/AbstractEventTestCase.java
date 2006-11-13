package com.thoughtworks.frankenstein.events;

import org.jmock.MockObjectTestCase;

import java.beans.PropertyVetoException;
import java.io.File;

import com.thoughtworks.frankenstein.common.WaitForIdle;

import javax.swing.*;

import spin.over.CheckingRepaintManager;

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

    public abstract void testPlay() throws Exception;


    protected void setUp() throws Exception {
        super.setUp();
        RepaintManager.setCurrentManager(new CheckingRepaintManager());
    }

    public void testSavesAndRestoresEventFromScriptLine() throws Exception {
        FrankensteinEvent event = createEvent();
        assertEquals(event, createEventFromScriptLine(event.getClass(), event.scriptLine()));
    }

    private FrankensteinEvent createEventFromScriptLine(Class aClass, String scriptLine) throws Exception {
        DefaultEventRegistry eventRegistry = new DefaultEventRegistry();
        eventRegistry.registerEvent(aClass);
        return eventRegistry.createEvent(scriptLine);
    }

    private void checkHasStringConstructor(Class eventClass) throws Exception {
         eventClass.getConstructor(new Class[] {String.class});
    }

    protected abstract FrankensteinEvent createEvent();

    protected void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

    protected String file(String s) {
          return s.replaceAll("/", (File.separator=="/")?File.separator:("\\"+File.separator));
    }
}

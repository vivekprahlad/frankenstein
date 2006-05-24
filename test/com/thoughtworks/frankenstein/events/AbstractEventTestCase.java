package com.thoughtworks.frankenstein.events;

import org.jmock.MockObjectTestCase;

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

    public abstract void testPlay();
}

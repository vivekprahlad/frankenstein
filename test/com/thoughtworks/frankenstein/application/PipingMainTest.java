package com.thoughtworks.frankenstein.application;

import junit.framework.TestCase;

/**
 * Ensures behaviour of PipingMain
 */
public class PipingMainTest extends TestCase {

    public void testParsesZeroArguments() throws ClassNotFoundException {
        String[] args = PipingMain.parseArgs(new String[] {"com.thoughtworks.frankenstein.application.TestMain"});
        assertEquals(0, args.length);
    }

    public void testParsesTwoArguments() throws ClassNotFoundException {
        String[] args = PipingMain.parseArgs(new String[] {"com.thoughtworks.frankenstein.application.TestMain", "one", "two"});
        assertEquals(2, args.length);
        assertEquals("one", args[0]);
        assertEquals("two", args[1]);
    }

    public void testParsesClass() throws ClassNotFoundException {
        Class mainClass = PipingMain.parseClass(new String[] {"com.thoughtworks.frankenstein.application.TestMain", "one", "two"});
        assertSame(mainClass, TestMain.class);
    }
}

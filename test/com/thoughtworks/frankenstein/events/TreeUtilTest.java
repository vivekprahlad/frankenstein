package com.thoughtworks.frankenstein.events;

import junit.framework.TestCase;

/**
 * Ensures behaviour of the TreeUtil class
 */
public class TreeUtilTest extends TestCase {

    public void testName() {
        assertEquals("\"name\"", TreeUtil.name("\"name\",\"one\",\"two\",\"three&#x83;four\""));
    }

    public void testpath() {
        String[] expectedPath = new String[]{"\"one\"", "\"two\"", "\"three,four\""};
        String[] actualPath = TreeUtil.path("\"name\",\"one\",\"two\",\"three&#x83;four\"");
        assertEquals(expectedPath.length, actualPath.length);
        for (int indexOfPath = 0; indexOfPath < expectedPath.length; indexOfPath++) {
            assertEquals(expectedPath[indexOfPath], actualPath[indexOfPath]);
        }
    }

    public void testPathString() {
        String[] path = new String[]{"\"one\"", "\"two\"", "\"three,four\""};
        String expectedPathString = "\"one\",\"two\",\"three&#x83;four\"";
        assertEquals(expectedPathString, TreeUtil.pathString(path, ","));
    }
}

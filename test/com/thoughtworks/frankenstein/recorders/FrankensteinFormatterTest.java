package com.thoughtworks.frankenstein.recorders;

import junit.framework.TestCase;

import java.util.logging.LogRecord;
import java.util.logging.Level;

/**
 * Ensures behaviour of FrankensteinFormatter
 * @author vivek
 */
public class FrankensteinFormatterTest extends TestCase {

    public void testFormatsMessagesWithLogLevelDateAndMessage() {
        LogRecord record = new LogRecord(Level.INFO, "Message");
        record.setMillis(1000);
        assertEquals("INFO: 01/01/1970 12:00:01 AM Message\n", new FrankensteinFormatter().format(record));
    }

    public void testFormatsExceptionMessagesWithLogLevelDateAndMessage() {
        LogRecord record = new LogRecord(Level.INFO, "Message");
        record.setMillis(1000);
        record.setThrown(createException());
        assertEquals("INFO: 01/01/1970 12:00:01 AM Exception: Foo\n" +
                "\t\t\t\tcom.test.class.someMethod(Test.java:10)\n" +
                "Message\n", new FrankensteinFormatter().format(record));
    }

    private RuntimeException createException() {
        RuntimeException exception = new RuntimeException("Foo");
        exception.setStackTrace(new StackTraceElement[] {new StackTraceElement("com.test.class", "someMethod", "Test.java", 10)});
        return exception;
    }
}

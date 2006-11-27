package com.thoughtworks.frankenstein.application;

import junit.framework.TestCase;

import java.util.logging.Logger;
import java.util.logging.MemoryHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import spin.over.EDTRuleViolation;

import javax.swing.*;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;

/**
 * Ensures behaviour of EDTRuleViolationLoggingRepaintManager
 * @author vivek
 */
public class EDTRuleViolationLoggingRepaintManagerTest extends MockObjectTestCase {

    public void testLogsEdtViolationsToFrankensteinLogger() {
        MockHandler handler = new MockHandler();
        Logger.getLogger("Frankenstein").addHandler(handler);
        EDTRuleViolation ruleViolation = violation();
        new EDTRuleViolationLoggingRepaintManager().indicate(ruleViolation);
        assertNotNull(handler.record);
        assertEquals("Frankenstein", handler.record.getLoggerName());
        assertEquals("com.thoughtworks.frankenstein.application.EDTRuleViolationLoggingRepaintManager",
                handler.record.getSourceClassName());
        assertSame(ruleViolation, handler.record.getThrown());
    }

    private EDTRuleViolation violation() {
        EDTRuleViolation ruleViolation = new EDTRuleViolation(new JPanel());
        ruleViolation.setStackTrace(new StackTraceElement[] {
                new StackTraceElement("FooClass", "fooMethod", "File.java", 0)
        });
        return ruleViolation;
    }

    private class MockHandler extends Handler {
        private LogRecord record;

        public void publish(LogRecord record) {
            this.record = record;
        }

        public void flush() {
        }

        public void close() throws SecurityException {
        }
    }
}

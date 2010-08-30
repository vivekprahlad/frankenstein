package com.thoughtworks.frankenstein.drivers;

import junit.framework.TestCase;

/**
 * Used to throw an exception when the event execution fails.
 *
 * @author Pavan
 * @author Prakash
 */
public class FailFastReporterTest extends TestCase {
    public void testIfReportFailureThrowsAnException() {
        FailFastReporter failFastReporter = new FailFastReporter();
        Exception exception = new Exception();
        try {
            failFastReporter.reportFailure(null, exception, null);
            fail("FailFastReporter's reportFailure method should fail by throwing exception.");
        } catch (Exception ex) {
        }
    }
}

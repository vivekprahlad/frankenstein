package com.thoughtworks.frankenstein.script;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.recorders.ScreenShot;

/**
 * Ensures behaviour of HtmlTestReporter
 */
public class HtmlTestReporterTest extends MockObjectTestCase {
    private HtmlTestReporter reporter;
    private Mock screenShot;

    protected void setUp() throws Exception {
        super.setUp();
        screenShot = mock(ScreenShot.class);
        reporter = new HtmlTestReporter((ScreenShot) screenShot.proxy());
    }

    public void testReportsTestSuccess() throws IOException {
        Mock frankensteinEvent = mock(FrankensteinEvent.class);
        expectActionTargetAndParameters(frankensteinEvent, "Parameters");
        reporter.startTest("testName");
        reporter.reportSuccess((FrankensteinEvent) frankensteinEvent.proxy());
        reporter.finishTest();
        assertEquals("<html>\n<head><title>testName</title></head>\n<body>\n<h3>testName</h3>\n" +
                "<h4>Test Status</h4>\n" +
                "<table BORDER CELLSPACING=0 CELLPADDING=4>\n" +
                "<tr><td bgcolor=#CFFFCF><font size=2 color=black>Action</font></td>" +
                "<td bgcolor=#CFFFCF><font size=2 color=black>Target</font></td>" +
                "<td bgcolor=#CFFFCF><font size=2 color=black>Parameters</font></td></tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>", reporter.report);
    }

    public void testReportsTestFailure() throws IOException {
        Mock frankensteinEvent = mock(FrankensteinEvent.class);
        expectActionTargetAndParameters(frankensteinEvent, "Parameters");
        screenShot.expects(once()).method("capture").will(returnValue("screenshot/screen1.png"));
        reporter.startTest("testName");
        reporter.reportFailure((FrankensteinEvent) frankensteinEvent.proxy(), new RuntimeException("Some Exception"), null);
        reporter.finishTest();
        assertEquals("<html>\n<head><title>testName</title></head>\n<body>\n<h3>testName</h3>\n" +
                "<h4>Test Status</h4>\n" +
                "<table BORDER CELLSPACING=0 CELLPADDING=4>\n" +
                "<tr><td bgcolor=#FFCFCF><font size=2 color=black>Action</font></td>" +
                "<td bgcolor=#FFCFCF><font size=2 color=black>Target<br>" +
                "<br><a href='screenshot/screen1.png'>Screen shot</a><br>" +
                "<br>Cause: Some Exception</font></td>" +
                "<td bgcolor=#FFCFCF><font size=2 color=black>Parameters</font></td></tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>", reporter.report);
    }

    public void testReportsCauseFromInvocationTargetException() throws IOException {
        Mock frankensteinEvent = mock(FrankensteinEvent.class);
        expectActionTargetAndParameters(frankensteinEvent, "Parameters");
        reporter.startTest("testName");
        screenShot.expects(once()).method("capture").will(returnValue("screenshot/screen1.png"));
        reporter.reportFailure((FrankensteinEvent) frankensteinEvent.proxy(),
                new RuntimeException(new InvocationTargetException(new RuntimeException("Some Exception"))), null);
        reporter.finishTest();
        assertEquals("<html>\n<head><title>testName</title></head>\n<body>\n<h3>testName</h3>\n" +
                "<h4>Test Status</h4>\n" +
                "<table BORDER CELLSPACING=0 CELLPADDING=4>\n" +
                "<tr><td bgcolor=#FFCFCF><font size=2 color=black>Action</font></td>" +
                "<td bgcolor=#FFCFCF><font size=2 color=black>Target<br><br><a href='screenshot/screen1.png'>Screen shot</a><br><br>Cause: " +
                "Some Exception</font></td>" +
                "<td bgcolor=#FFCFCF><font size=2 color=black>Parameters</font></td></tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>", reporter.report);
    }

    public void testSubstitutesNbspForEmptyStrings() throws IOException {
        Mock frankensteinEvent = mock(FrankensteinEvent.class);
        expectActionTargetAndParameters(frankensteinEvent, "");
        reporter.startTest("testName");
        screenShot.expects(once()).method("capture").will(returnValue("screenshot/screen1.png"));
        reporter.reportFailure((FrankensteinEvent) frankensteinEvent.proxy(), new RuntimeException("Some Exception"), null);
        reporter.finishTest();
        assertEquals("<html>\n<head><title>testName</title></head>\n<body>\n<h3>testName</h3>\n" +
                "<h4>Test Status</h4>\n" +
                "<table BORDER CELLSPACING=0 CELLPADDING=4>\n" +
                "<tr><td bgcolor=#FFCFCF><font size=2 color=black>Action</font></td>" +
                "<td bgcolor=#FFCFCF><font size=2 color=black>Target<br><br><a href='screenshot/screen1.png'>" +
                "Screen shot</a><br><br>Cause: Some Exception</font></td>" +
                "<td bgcolor=#FFCFCF><font size=2 color=black>&nbsp;</font></td></tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>", reporter.report);
    }

    private void expectActionTargetAndParameters(Mock frankensteinEvent, String parameters) {
        frankensteinEvent.expects(once()).method("action").will(returnValue("Action"));
        frankensteinEvent.expects(once()).method("target").will(returnValue("Target"));
        frankensteinEvent.expects(once()).method("parameters").will(returnValue(parameters));
    }

    public void testExtractsNameWhenNoPatchIsSpecified() {
        assertEquals("TestName", reporter.extractTestName("TestName"));
    }

    public void testExtractsNameGivenPathOnWindows() {
        assertEquals("TestName", reporter.extractTestName("C:/TestDir/TestName"));
    }

    public void testExtractsNameGivenPathWithBackSlashesOnWindows() {
        assertEquals("TestName", reporter.extractTestName("C:\\TestDir\\TestName"));
    }

    public void testExtractsNameGivenPathOnUnix() {
        assertEquals("TestName", reporter.extractTestName("/home/user/reportdir/TestName"));
    }
}

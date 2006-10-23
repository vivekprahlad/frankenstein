package com.thoughtworks.frankenstein.script;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.awt.*;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.recorders.DefaultScreenShot;
import com.thoughtworks.frankenstein.recorders.ScreenShot;

/**
 * Understands creating html test reports
 *
 * @author Vivek Prahlad
 */
public class HtmlTestReporter implements TestReporter {
    private String testName = "test";
    private String body = INITIAL_BODY;
    private static final String RED = "#FFCFCF";
    private static final String GREEN = "#CFFFCF";
    private static final String INITIAL_BODY = "<table BORDER CELLSPACING=0 CELLPADDING=4>\n";
    private String testFileName = testName + ".html";
    private static final String SCREENSHOT_DIRECTORY = "screenshots";
    private ScreenShot screenShot;

    public HtmlTestReporter(ScreenShot screenShot) {
        this.screenShot = screenShot;
    }

    public HtmlTestReporter() {
        this(new DefaultScreenShot());
    }

    public void startTest(String testName) {
        this.testName = extractTestName(testName);
        this.testFileName = testName + ".html";
        body = INITIAL_BODY;
    }

    protected String extractTestName(String testName) {
        String test = testName.replaceAll("\\\\", "/");
        return test.indexOf("/") == -1 ? test : test.substring(test.lastIndexOf('/') + 1, test.length());
    }

    public void reportSuccess(FrankensteinEvent event) {
        body += line(event, GREEN);
    }

    private String line(FrankensteinEvent event, String background) {
        return "<tr>" + td(background, event.action()) + td(background, event.target())
                + td(background, event.parameters()) + "</tr>\n";
    }

    private String line(FrankensteinEvent event, String background, Exception cause, String imageFileName) {
        return "<tr>" + td(background, event.action()) + td(background, event.target() + "<br><br><a href='" + imageFileName+"'>Screen shot</a>" + "<br><br>Cause: " +exception(cause))
                + td(background, event.parameters()) + "</tr>\n";
    }

    private String exception(Exception cause) {
        if (cause instanceof RuntimeException) {
            RuntimeException rtException = (RuntimeException) cause;
            if (rtException.getCause() instanceof InvocationTargetException) {
                InvocationTargetException invocationTargetException = (InvocationTargetException) rtException.getCause();
                return invocationTargetException.getCause().getMessage();
            }
        }
        return cause.getMessage();
    }

    private String td(String background, String value) {
        return "<td bgcolor=" + background + "><font size=2 color=black>" + valueString(value) + "</font></td>";
    }

    private String valueString(String value) {
        return "".equals(value) ? "&nbsp;" : value;
    }

    String report() {
        return header() + body;
    }

    private String header() {
        return "<html>\n<head><title>" + testName + "</title></head>\n<body>\n"
                + "<h3>" + testName + "</h3>\n" + "<h4>Test Status</h4>\n";
    }

    public void reportFailure(FrankensteinEvent event, Exception cause, Robot robot) {
        File reportFile = new File(testFileName);
        body += line(event, RED, cause, screenShot.capture(reportFile.getParent(), robot));
    }

    public String finishTest() throws IOException {
        body += "</table>\n</body>\n</html>";
        writeReport();
        String retVal = report();
        body = INITIAL_BODY;
        return retVal;
    }

    private void writeReport() throws IOException {
        File reportFile = new File(testFileName);
        if (reportFile.getParent()!=null) {
            new File(reportFile.getParent()).mkdirs();
        }
        if (reportFile.exists()) {
            reportFile.delete();
            reportFile.createNewFile();
        }
        writeReport(reportFile);
    }

    private void writeReport(File reportFile) throws IOException {
        FileWriter writer = new FileWriter(reportFile, false);
        writer.write(report());
        writer.flush();
        writer.close();
    }
}

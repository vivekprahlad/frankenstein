package com.thoughtworks.frankenstein.script;

import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Understands creating html test reports
 * @author Vivek Prahlad
 */
public class HtmlTestReporter implements TestReporter {
    private String testName = "test.html";
    private String body = INITIAL_BODY;
    private static final String RED = "#FFCFCF";
    private static final String GREEN = "#CFFFCF";
    private static final String INITIAL_BODY = "<table BORDER CELLSPACING=0 CELLPADDING=4>\n";

    public void startTest(String testName) {
        this.testName = testName;
        body = INITIAL_BODY;
    }

    public void reportSuccess(FrankensteinEvent event) {
        body += line(event, GREEN);
    }

    private String line(FrankensteinEvent event, String background) {
        return "<tr>" + td(background, event.action()) + td(background, event.target())
                + td(background, event.parameters()) + "</tr>\n";
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

    public void reportFailure(FrankensteinEvent event, Exception cause) {
        body += line(event, RED);
    }

    public void finishTest() throws IOException {
        body += "</table>\n</body>";
        writeReport();
    }

    private void writeReport() throws IOException {
        FileWriter writer = new FileWriter(fileName(), false);
        writer.write(report());
        writer.flush();
        writer.close();
    }

    private String fileName() {
        return testName + ".html";
    }
}

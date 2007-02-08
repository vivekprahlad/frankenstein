package com.thoughtworks.frankenstein.script;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Understands presenting a TestReporter interface for a collection of reporters.
 *
 * @author Vivek Prahlad
 */
public class CompositeReporter implements TestReporter {
    private List reporters = new ArrayList();

    public static CompositeReporter create(TestReporter defaultReporter) {
        CompositeReporter reporter = new CompositeReporter();
        reporter.addTestReporter(defaultReporter);
        return reporter;
    }

    public void startTest(String testName) {
        for (Iterator iterator = reporters.iterator(); iterator.hasNext();) {
            ((TestReporter) iterator.next()).startTest(testName);
        }
    }

    public void reportSuccess(FrankensteinEvent event) {
        for (Iterator iterator = reporters.iterator(); iterator.hasNext();) {
            ((TestReporter) iterator.next()).reportSuccess(event);
        }
    }

    public void reportFailure(FrankensteinEvent event, Exception cause, Robot robot) {
        for (Iterator iterator = reporters.iterator(); iterator.hasNext();) {
            ((TestReporter) iterator.next()).reportFailure(event, cause, robot);
        }
    }

    public void finishTest() {
        for (Iterator iterator = reporters.iterator(); iterator.hasNext();) {
            ((TestReporter) iterator.next()).finishTest();
        }
    }

    public void addTestReporter(TestReporter reporter) {
        reporters.add(reporter);
    }

    public void clear() {
        reporters.clear();
    }
}

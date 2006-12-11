package com.thoughtworks.frankenstein.script;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.IOException;

import com.thoughtworks.frankenstein.events.ActivateWindowEvent;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.common.RobotFactory;

/**
 * Ensures behaviour of CompositeReporter
 * @author Vivek Prahlad
 */
public class CompositeReporterTest extends MockObjectTestCase {
    private CompositeReporter reporter;
    private Mock mockReporter;

    protected void setUp() throws Exception {
        super.setUp();
        reporter = new CompositeReporter();
        mockReporter = mock(TestReporter.class);
    }

    public void testDelegatesTestStartToAddedReporters() {
        mockReporter.expects(once()).method("startTest").with(eq("foo/test/name"));
        reporter.addTestReporter((TestReporter) mockReporter.proxy());
        reporter.startTest("foo/test/name");
    }

    public void testDelegatesTestFinishTestToAddedReporters() throws IOException {
        mockReporter.expects(once()).method("finishTest");
        reporter.addTestReporter((TestReporter) mockReporter.proxy());
        reporter.finishTest();
    }

    public void testDelegatesStepSuccessToAddedReporters() {
        FrankensteinEvent event = new ActivateWindowEvent("title");
        mockReporter.expects(once()).method("reportSuccess").with(same(event));
        reporter.addTestReporter((TestReporter) mockReporter.proxy());
        reporter.reportSuccess(event);
    }

    public void testDelegatesStepFailureToAddedReporters() {
        FrankensteinEvent event = new ActivateWindowEvent("title");
        Exception exception = new Exception();
        mockReporter.expects(once()).method("reportFailure").with(same(event), same(exception), same(RobotFactory.getRobot()));
        reporter.addTestReporter((TestReporter) mockReporter.proxy());
        reporter.reportFailure(event, exception, RobotFactory.getRobot());
    }

    public void testRemovesAllAddedReporters() {
        FrankensteinEvent event = new ActivateWindowEvent("title");
        Exception exception = new Exception();
        mockReporter.expects(never()).method(ANYTHING);
        reporter.addTestReporter((TestReporter) mockReporter.proxy());
        reporter.clear();
        reporter.startTest("test");
        reporter.reportSuccess(event);
        reporter.reportFailure(event, exception, RobotFactory.getRobot());
        reporter.finishTest();
    }
}
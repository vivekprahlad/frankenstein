package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

import com.thoughtworks.frankenstein.application.WorkerThreadMonitor;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Takes care of test reporting etc.
 * @author Vivek Prahlad
 */
public class DefaultScriptContext implements ScriptContext {
    private TestReporter reporter;
    private Robot robot;
    private WorkerThreadMonitor monitor;
    private WindowContext context;
    private ComponentFinder finder;

    public DefaultScriptContext(TestReporter reporter, WorkerThreadMonitor monitor, WindowContext context, ComponentFinder finder) {
        this.reporter = reporter;
        this.monitor = monitor;
        this.context = context;
        this.finder = finder;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void startTest(String testName) {
        reporter.startTest(testName);
    }

    public void play(List events) {
        monitor.start();
        playEvent(events);
        finishTest();
    }

    private void playEvent(List events) {
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            waitForIdle();
            play((FrankensteinEvent) iterator.next());
        }
    }

    protected void play(FrankensteinEvent event) {
        try {
            event.play(context, finder, this, robot);
            reporter.reportSuccess(event);
        } catch (Exception e) {
            reporter.reportFailure(event, e);
        }
    }

    protected void finishTest() {
        try {
            reporter.finishTest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForIdle() {
        monitor.waitForIdle();
        while (EventQueue.getCurrentEvent() != null) {
            monitor.waitForIdle();
            robot.waitForIdle();
        }
    }
}

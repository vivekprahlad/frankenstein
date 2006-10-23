package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.*;
import java.io.IOException;

import com.thoughtworks.frankenstein.application.WorkerThreadMonitor;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.script.TestReporter;
import com.thoughtworks.frankenstein.script.HtmlTestReporter;

/**
 * Takes care of test reporting etc.
 *
 * @author Vivek Prahlad
 */
public class DefaultScriptContext implements ScriptContext {
    private TestReporter reporter;
    private Robot robot;
    private WorkerThreadMonitor monitor;
    private WindowContext context;
    private ComponentFinder finder;
    private boolean testPassed = true;
    private Logger logger;

    public DefaultScriptContext(TestReporter reporter,
                                WorkerThreadMonitor monitor,
                                WindowContext context,
                                ComponentFinder finder) {
        this.reporter = reporter;
        this.monitor = monitor;
        this.context = context;
        this.finder = finder;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        createLogger();
    }

    public DefaultScriptContext(WorkerThreadMonitor monitor,
                                WindowContext context,
                                ComponentFinder finder) {
        this(new HtmlTestReporter(), monitor, context, finder);
    }

    private void createLogger() {
        logger = Logger.getLogger("Frankenstein");
        logger.setUseParentHandlers(false);
        try {
            addHandler(logger, new ConsoleHandler());
            addHandler(logger, new FileHandler("frankenstein.log"));
            logger.setLevel(Level.OFF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addHandler(Logger logger, Handler consoleHandler) {
        consoleHandler.setFormatter(new FrankensteinFormatter());
        logger.addHandler(consoleHandler);
    }

    public void startTest(String testName) {
        reporter.startTest(testName);
        testPassed = true;
    }

    public void play(List events) {
        monitor.start();
        playEvents(events);
        finishTest();
    }

    public boolean isScriptPassed() {
        return testPassed;
    }

    private void playEvents(List events) {
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            waitForIdle();
            FrankensteinEvent event = (FrankensteinEvent) iterator.next();
            play(event);
        }
    }

    protected void play(FrankensteinEvent event) {
        try {
            logger.info("Playing: " + event);
            event.play(context, finder, this, robot);
            reporter.reportSuccess(event);
        } catch (Exception e) {
            logger.log(Level.WARNING, "", e);
            testPassed = false;
            reporter.reportFailure(event, e, robot);
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
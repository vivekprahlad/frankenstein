package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.*;

import com.thoughtworks.frankenstein.application.WorkerThreadMonitor;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.script.TestReporter;

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
    private List scriptListeners = new ArrayList();

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
        startMonitor();
        playEvents(events);
        finishTest();
    }

    public void startMonitor() {
        monitor.start();
    }

    public void play(FrankensteinEvent event) {
        waitForIdle();
        playAndReportTestStatus(event);
    }

    public boolean isScriptPassed() {
        return testPassed;
    }

    public void addScriptListener(ScriptListener scriptListener) {
        scriptListeners.add(scriptListener);
    }

    public void removeScriptListener(ScriptListener listener) {
        scriptListeners.remove(listener);
    }

    private void playEvents(List events) {
        int eventIndex = 0;
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            waitForIdle();
            fireScriptStepStartedEvent(eventIndex++);
            playAndReportTestStatus((FrankensteinEvent) iterator.next());
        }
        fireScriptCompletedEvent();
    }

    private void fireScriptCompletedEvent() {
        for (Iterator iterator = scriptListeners.iterator(); iterator.hasNext();) {
            ScriptListener scriptListener = (ScriptListener) iterator.next();
            scriptListener.scriptCompleted(isScriptPassed());
        }
    }

    private void fireScriptStepStartedEvent(int eventIndex) {
        for (Iterator iterator = scriptListeners.iterator(); iterator.hasNext();) {
            ScriptListener scriptListener = (ScriptListener) iterator.next();
            scriptListener.scriptStepStarted(eventIndex);
        }
    }

    protected void playAndReportTestStatus(FrankensteinEvent event) {
        try {
            logger.info("Playing: " + event);
            event.play(context, finder, this, robot);
            reporter.reportSuccess(event);
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "", e);
            testPassed = false;
            reporter.reportFailure(event, e, robot);
        }
    }

    protected void finishTest() {
        reporter.finishTest();
    }

    private void waitForIdle() {
        monitor.waitForIdle();
        context.waitForProgressBar();
        while (EventQueue.getCurrentEvent() != null) {
            monitor.waitForIdle();
            robot.waitForIdle();
        }
    }
}
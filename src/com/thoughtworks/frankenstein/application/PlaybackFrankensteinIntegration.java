package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.drivers.FailFastReporter;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultComponentFinder;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.DefaultScriptContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.script.CompositeReporter;
import com.thoughtworks.frankenstein.script.HtmlTestReporter;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Playback specific FrankensteinIntegration
 */
public class PlaybackFrankensteinIntegration extends FrankensteinIntegration {
    private ScriptContext scriptContext;

    public PlaybackFrankensteinIntegration(Class mainClass) {
        this(mainClass, new HtmlTestReporter());
    }

    public PlaybackFrankensteinIntegration(Class mainClass, TestReporter testReporter) {
        this(mainClass, testReporter, new RegexWorkerThreadMonitor("UIWorker"));
    }

    public PlaybackFrankensteinIntegration(Class mainClass,
                                           TestReporter testReporter,
                                           WorkerThreadMonitor threadMonitor) {
        this(mainClass, testReporter, threadMonitor, new DefaultComponentFinder(new DefaultNamingStrategy()), new DefaultWindowContext());
    }

    public PlaybackFrankensteinIntegration(Class mainClass,
                                           TestReporter testReporter,
                                           WorkerThreadMonitor threadMonitor,
                                           ComponentFinder componentFinder,
                                           WindowContext windowContext) {
        super(mainClass);
        this.recorderMode = RecorderMode.PLAY_MODE;
        this.testReporter = createCompositeReporter(testReporter);
        this.scriptContext = new DefaultScriptContext(this.testReporter, threadMonitor, windowContext, componentFinder);
        application = createApplication(mainClass);
    }

    public PlaybackFrankensteinIntegration(Application application) {
        this(application, new HtmlTestReporter());
    }

    public PlaybackFrankensteinIntegration(Application application, TestReporter testReporter) {
        this(application, testReporter, new RegexWorkerThreadMonitor("UIWorker"));
    }

    public PlaybackFrankensteinIntegration(Application application,
                                           TestReporter testReporter,
                                           RegexWorkerThreadMonitor threadMonitor) {
        this(application, testReporter, threadMonitor, new DefaultComponentFinder(new DefaultNamingStrategy()), new DefaultWindowContext());
    }

    public PlaybackFrankensteinIntegration(Application application,
                                           TestReporter testReporter,
                                           WorkerThreadMonitor threadMonitor,
                                           ComponentFinder componentFinder,
                                           WindowContext windowContext) {
        super(application);
        this.recorderMode = RecorderMode.PLAY_MODE;
        this.testReporter = createCompositeReporter(testReporter);
        this.scriptContext = new DefaultScriptContext(this.testReporter, threadMonitor, windowContext, componentFinder);
        this.application = application;
    }

    private Application createApplication(Class mainClass) {
        return new CommandLineApplication(mainClass);
    }

    private CompositeReporter createCompositeReporter(TestReporter testReporter) {
        CompositeReporter compositeReporter = new CompositeReporter();
        compositeReporter.addTestReporter(testReporter);
        compositeReporter.addTestReporter(new FailFastReporter());
        return compositeReporter;
    }

    public void setScriptContext(ScriptContext scriptContext) {
        this.scriptContext = scriptContext;
        this.scriptContext.startMonitor();
    }

    public TestReporter getTestReporter() {
        return testReporter;
    }

    public void record() {
    }

    public void stop() {
        testReporter.finishTest();
        socketListener.stop();
    }

    public void createRecorderUI(FrankensteinRecorder compositeRecorder) {
    }

    public void play(FrankensteinEvent event) {
        scriptContext.play(event);
    }
}
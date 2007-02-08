package com.thoughtworks.frankenstein.application;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import com.thoughtworks.frankenstein.common.Constants;
import com.thoughtworks.frankenstein.common.DefaultComponentDecoder;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.playback.*;
import com.thoughtworks.frankenstein.recorders.DefaultRecorder;
import com.thoughtworks.frankenstein.recorders.DefaultScriptContext;
import com.thoughtworks.frankenstein.recorders.Recorder;
import com.thoughtworks.frankenstein.script.CompositeReporter;
import com.thoughtworks.frankenstein.script.HtmlTestReporter;
import com.thoughtworks.frankenstein.script.TestReporter;
import com.thoughtworks.frankenstein.ui.DefaultFileDialogLauncher;
import com.thoughtworks.frankenstein.ui.RecorderPane;
import com.thoughtworks.frankenstein.ui.RecorderTableModel;

/**
 * Facade for the Frankenstein framework. Provides customization hooks.
 *
 * @author Vivek Prahlad
 */
public class FrankensteinIntegration {
    protected Recorder eventRecorder;
    private DefaultFrankensteinRecorder recorder;
    private Application application;
    private JFrame frame;
    private WindowContext context;
    private RecorderMode recorderMode;
    private SocketListener socketListener;
    private int port = Constants.LISTEN_PORT;//Default value
    private CompositeReporter testReporter;

    public FrankensteinIntegration(Class mainClass,
                                   JFrame frame,
                                   WorkerThreadMonitor monitor,
                                   WindowContext context,
                                   NamingStrategy namingStrategy, RecorderMode recorderMode) {
        this(mainClass, frame, monitor, context, namingStrategy, new DefaultComponentFinder(namingStrategy), recorderMode);
    }

    public FrankensteinIntegration(Class mainClass,
                                   JFrame frame,
                                   WorkerThreadMonitor monitor,
                                   WindowContext context,
                                   NamingStrategy namingStrategy,
                                   ComponentFinder finder, RecorderMode recorderMode) {
        this(new CommandLineApplication(mainClass), frame, monitor, context, namingStrategy, finder, recorderMode);
    }

    public FrankensteinIntegration(Application application,
                                   JFrame frame,
                                   WorkerThreadMonitor monitor,
                                   WindowContext context,
                                   NamingStrategy namingStrategy,
                                   ComponentFinder finder,
                                   RecorderMode recorderMode) {
        this.application = application;
        this.frame = frame;
        this.context = context;
        this.recorderMode = recorderMode;
        this.testReporter = CompositeReporter.create(new HtmlTestReporter());
        eventRecorder = new DefaultRecorder(new DefaultScriptContext(testReporter, monitor, context, finder));
        recorder = new DefaultFrankensteinRecorder(eventRecorder, new DefaultComponentDecoder(), namingStrategy);
        socketListener = new SocketListener(recorder);
        createRecorderUI(recorder);
    }

    public FrankensteinIntegration(Class mainClass, WorkerThreadMonitor monitor, RecorderMode recorderMode) {
        this(mainClass, new JFrame("Recorder"), monitor, new DefaultWindowContext(), new DefaultNamingStrategy(), recorderMode);
    }

    public FrankensteinIntegration(Class mainClass, RecorderMode recorderMode) {
        this(mainClass, new JFrame("Recorder"), new RegexWorkerThreadMonitor("UIWorker"), new DefaultWindowContext(), new DefaultNamingStrategy(), recorderMode);
    }

    public void registerAction(Class actionClass) {
        recorder.registerAction(actionClass);
    }

    public void registerEvent(Class eventClass) {
        recorder.registerEvent(eventClass);
    }

    public void registerRecorder(Class recorderClass) {
        recorder.registerRecorder(recorderClass);
    }

    public void setLogLevel(Level level) {
        Logger.getLogger("Frankenstein").setLevel(level);
    }

    /**
     * Set the port at which Frankenstein listens for commands. (The default port is 5678)
     * <p/>
     * * @param port port at which Frankenstein listens.
     */
    public void setPort(int port) {
        this.port = port;
    }

    public void start(String[] args) {
        logInfo();
        recorderMode.start(recorder);
        socketListener.start(port);
        application.launch(args);
    }

    public void record() {
        recorder.start();
    }

    public void stop() {
        context.close();
        recorderMode.stop(recorder);
        socketListener.stop();
        frame.dispose();
    }

    public void addTestReporter(TestReporter reporter) {
        testReporter.addTestReporter(reporter);
    }

    public void removeAllTestReporters() {
        testReporter.clear();
    }

    public void logInfo() {
        Logger.getLogger("Frankenstein").setLevel(Level.INFO);
    }

    private void createRecorderUI(FrankensteinRecorder compositeRecorder) {
        frame.getContentPane().add(new RecorderPane(compositeRecorder, new DefaultFileDialogLauncher(), new RecorderTableModel(eventRecorder),
                new PlaybackSpeedControlScriptListener(Constants.DEFAULT_DELAY, Constants.DEFAULT_SHOULD_SLOW_DOWN)));
        frame.pack();
        frame.setVisible(true);
    }
}

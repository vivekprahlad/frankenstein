package com.thoughtworks.frankenstein.application;

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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Facade for the Frankenstein framework. Provides customization hooks.
 *
 * @author Vivek Prahlad
 */
public class FrankensteinIntegration implements FrankensteinIntegrationIf, ChangeListener {
    protected Recorder eventRecorder;
    protected DefaultFrankensteinRecorder recorder;
    protected Application application;
    protected JFrame frame;
    protected WindowContext context;
    protected RecorderMode recorderMode;
    protected SocketListener socketListener;
    protected int port = Constants.LISTEN_PORT;//Default value
    protected CompositeReporter testReporter;
    private PlaybackSpeedControlScriptListener playbackControlListener;
    private List recordEventListeners;

    public FrankensteinIntegration(Class mainClass,
                                          JFrame frame,
                                          WorkerThreadMonitor monitor,
                                          WindowContext context,
                                          NamingStrategy namingStrategy) {
        this(mainClass, frame, monitor, context, namingStrategy, new DefaultComponentFinder(namingStrategy));
    }

    public FrankensteinIntegration(Class mainClass,
                                          JFrame frame,
                                          WorkerThreadMonitor monitor,
                                          WindowContext context,
                                          NamingStrategy namingStrategy,
                                          ComponentFinder finder) {
        this(new CommandLineApplication(mainClass), frame, monitor, context, namingStrategy, finder);
    }

    public FrankensteinIntegration(Application application,
                                          JFrame frame,
                                          WorkerThreadMonitor monitor,
                                          WindowContext context,
                                          NamingStrategy namingStrategy,
                                          ComponentFinder finder) {
        this.application = application;
        this.frame = frame;
        this.context = context;
        this.recorderMode = RecorderMode.RECORD_AND_PLAY_MODE;
        this.testReporter = CompositeReporter.create(new HtmlTestReporter());
        eventRecorder = new DefaultRecorder(new DefaultScriptContext(testReporter, monitor, context, finder));
        eventRecorder.addChangeListener(this);
        recorder = new DefaultFrankensteinRecorder(eventRecorder, new DefaultComponentDecoder(), namingStrategy);
        socketListener = new SocketListener(new BulkExecutionRequestProcessingStrategy(recorder));
        recorder.addScriptListener(socketListener);
        playbackControlListener = new PlaybackSpeedControlScriptListener(Constants.DEFAULT_DELAY, Constants.DEFAULT_SHOULD_SLOW_DOWN);
        recordEventListeners = new ArrayList();
        createRecorderUI(recorder);
    }

    public FrankensteinIntegration(Class mainClass, WorkerThreadMonitor monitor) {
        this(mainClass, new JFrame("Recorder"), monitor, new DefaultWindowContext(), new DefaultNamingStrategy());
    }

    public FrankensteinIntegration(Class mainClass) {
        this(mainClass, new JFrame("Recorder"), new RegexWorkerThreadMonitor("UIWorker"), new DefaultWindowContext(), new DefaultNamingStrategy());
    }

    public FrankensteinIntegration(Application application) {
        this(application, new JFrame("Recorder"), new RegexWorkerThreadMonitor("UIWorker"), new DefaultWindowContext(), new DefaultNamingStrategy());
    }

    public FrankensteinIntegration(Application application, JFrame frame, WorkerThreadMonitor monitor, WindowContext context, NamingStrategy namingStrategy) {
        this(application, frame, monitor, context, namingStrategy, new DefaultComponentFinder(namingStrategy));
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
     *
     * @param port port at which Frankenstein listens.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Changes the recorder mode. The two possible modes are RecorderMode.PLAY_MODE and RecorderMode.RECORD_AND_PLAY_MODE
     *
     * @param recorderMode the recorder mode.
     */
    public void setRecorderMode(RecorderMode recorderMode) {
        this.recorderMode = recorderMode;
    }

    /**
     * Sets the playback delay between steps.
     *
     * @param delay delay in milliseconds.
     */
    public void setPlaybackDelay(int delay) {
        playbackControlListener.setDelay(delay);
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

    public void createRecorderUI(FrankensteinRecorder compositeRecorder) {
        frame.getContentPane().add(new RecorderPane(compositeRecorder, new DefaultFileDialogLauncher(), new RecorderTableModel(eventRecorder),
                playbackControlListener));
        frame.pack();
        frame.setVisible(true);
    }

    public void addRecordEventListener(RecordEventListener listener) {
        recordEventListeners.add(listener);
    }

    public List recordedEvents() {
        return eventRecorder.eventList();
    }

    public void stateChanged(ChangeEvent e) {
        for (Iterator iterator = recordEventListeners.iterator(); iterator.hasNext();) {
            RecordEventListener recordEventListener = (RecordEventListener) iterator.next();
            recordEventListener.recorded(e);
        }
    }
}

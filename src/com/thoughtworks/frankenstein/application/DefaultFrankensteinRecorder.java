package com.thoughtworks.frankenstein.application;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.picocontainer.defaults.DefaultPicoContainer;

import com.thoughtworks.frankenstein.common.ComponentDecoder;
import com.thoughtworks.frankenstein.events.DefaultEventRegistry;
import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.recorders.*;
import com.thoughtworks.frankenstein.script.Script;

/**
 * Top level recorder
 * @author Vivek Prahlad
 */
public class DefaultFrankensteinRecorder implements FrankensteinRecorder {
    protected List recorders = new ArrayList();
    private DefaultEventRegistry registry = new DefaultEventRegistry();
    private Script script = new Script(registry);
    private DefaultPicoContainer container = new DefaultPicoContainer();
    private Recorder recorder;

    public DefaultFrankensteinRecorder(Recorder recorder, ComponentDecoder decoder, NamingStrategy namingStrategy) {
        this.recorder = recorder;
        container.registerComponentInstance(recorder);
        container.registerComponentInstance(decoder);
        container.registerComponentInstance(new DefaultComponentVisibility());
        container.registerComponentInstance(namingStrategy);
        container.start();
        script = new Script(registry);
        stop();
        createRecorders();
    }

    private void createRecorders() {
        registerRecorder(AssertLabelRecorder.class);
        registerRecorder(ButtonClickRecorder.class);
        registerRecorder(CheckBoxRecorder.class);
        registerRecorder(CheckTextRecorder.class);
        registerRecorder(ClickTableHeaderRecorder.class);
        registerRecorder(ListRecorder.class);
        registerRecorder(DialogRecorder.class);
        registerRecorder(FileChooserRecorder.class);
        registerRecorder(InternalFrameRecorder.class);
        registerRecorder(KeystrokeRecorder.class);
        registerRecorder(ListSelectionRecorder.class);
        registerRecorder(MenuNavigationRecorder.class);
        registerRecorder(MoveSliderRecorder.class);
        registerRecorder(RadiobuttonRecorder.class);
        registerRecorder(TableRowRecorder.class);
        registerRecorder(SelectDropDownRecorder.class);
        registerRecorder(SelectTableRecorder.class);
        registerRecorder(SelectTreeRecorder.class);
        registerRecorder(SpinnerRecorder.class);
        registerRecorder(TableEditRecorder.class);
        registerRecorder(TabSwitchRecorder.class);
        registerRecorder(TextFieldRecorder.class);
        registerRecorder(WindowActivationRecorder.class);
    }

    public void registerAction(Class actionClass) {
        registry.registerAction(actionClass);
    }

    public void registerEvent(Class frankensteinEvent) {
        registry.registerEvent(frankensteinEvent);
    }

    public void registerRecorder(Class recorderClass) {
        container.registerComponentImplementation(recorderClass);
        recorders.add(container.getComponentInstanceOfType(recorderClass));
    }

    public void startRecording() {
        for (Iterator iterator = recorders.iterator(); iterator.hasNext();) {
            ((ComponentRecorder) iterator.next()).register();
        }
    }

    public void stopRecording() {
        for (Iterator iterator = recorders.iterator(); iterator.hasNext();) {
            ((ComponentRecorder) iterator.next()).unregister();
        }
    }

    public void start() {
        recorder.start();
    }

    public void stop() {
        recorder.stop();
    }

    public void play() {
        recorder.play();
    }

    public void save(File file) throws IOException {
        String text = script.scriptText(recorder.eventList());
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.flush();
        fileWriter.close();
    }

    public void load(File file) throws IOException {
        FileReader reader = new FileReader(file);
        load(reader);
        reader.close();
    }

    public void load(Reader reader) throws IOException {
        recorder.setEventList(script.parse(reader));
    }

    public void addScriptListener(ScriptListener listener) {
        recorder.addScriptListener(listener);
    }

    public void removeScriptListener(ScriptListener listener) {
        recorder.removeScriptListener(listener);
    }

    public void reset() {
        recorder.reset();
    }
}

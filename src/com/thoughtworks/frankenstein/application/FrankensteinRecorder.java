package com.thoughtworks.frankenstein.application;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import com.thoughtworks.frankenstein.recorders.ScriptListener;

/**
 * Top level recorder interface
 * @author Vivek Prahlad
 */
public interface FrankensteinRecorder {
    void start();

    void stop();

    void play();

    void save(File file) throws IOException;

    void load(File selectedFile) throws IOException;

    void load(Reader reader) throws IOException;

    void reset();

    void registerAction(Class frankensteinEvent);

    void registerRecorder(Class recorderClass);

    void registerEvent(Class frankensteinEvent);

    void addScriptListener(ScriptListener listener);

    void removeScriptListener(ScriptListener listener);
}

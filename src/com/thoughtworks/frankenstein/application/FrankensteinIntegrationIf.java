package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * 
 */
public interface FrankensteinIntegrationIf {

    void setPort(int port);

    void start(String[] args);

    void record();

    void stop();

    void addTestReporter(TestReporter reporter);

    void removeAllTestReporters();

    void createRecorderUI(FrankensteinRecorder compositeRecorder);
}

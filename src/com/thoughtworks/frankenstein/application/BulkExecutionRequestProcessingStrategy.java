package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.common.Constants;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BulkExecutionRequestProcessingStrategy implements RequestProcessingStrategy {
    private FrankensteinRecorder recorder;
    private boolean passed;

    public BulkExecutionRequestProcessingStrategy(FrankensteinRecorder recorder) {
        this.recorder = recorder;
    }

    public void handleRequest(Reader reader, Writer writer) throws IOException {
        recorder.load(reader);
        recorder.play();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                Logger.getLogger("Frankenstein").log(Level.WARNING, "Error", e);
            }
        }
        writeScriptCompletion(writer);
        writer.close();
        reader.close();
    }

    public void completeRequest(boolean passed) {
        this.passed = passed;
        synchronized (this) {
            notifyAll();
        }
    }

    private void writeScriptCompletion(Writer writer) throws IOException {
        writer.write(passed ? Constants.SCRIPT_PASSED : Constants.SCRIPT_FAILED);
        writer.flush();
    }
}
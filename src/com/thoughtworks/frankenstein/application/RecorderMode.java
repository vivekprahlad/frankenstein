package com.thoughtworks.frankenstein.application;

/**
 * The enumeration that defines Frankenstein Recording Modes.
 */
public abstract class RecorderMode {

    private String description;

    private RecorderMode(String description) {
        this.description = description;
    }

    public abstract void start(FrankensteinRecorder recorder);

    public abstract void stop(FrankensteinRecorder recorder);

    public String toString() {
        return description;
    }

    public static final RecorderMode PLAY_MODE = new RecorderMode("Play Mode") {

        public void start(FrankensteinRecorder recorder) {
        }

        public void stop(FrankensteinRecorder recorder) {
        }
    };

    public static final RecorderMode RECORD_AND_PLAY_MODE = new RecorderMode("Record and play Mode") {

        public void start(FrankensteinRecorder recorder) {
            recorder.startRecording();
        }

        public void stop(FrankensteinRecorder recorder) {
            recorder.stopRecording();
        }
    };
}

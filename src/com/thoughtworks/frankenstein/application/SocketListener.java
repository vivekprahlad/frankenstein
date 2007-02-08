package com.thoughtworks.frankenstein.application;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thoughtworks.frankenstein.common.Constants;
import com.thoughtworks.frankenstein.recorders.ScriptListener;

/**
 * Listens for incoming requests from drivers.
 *
 * @author Vivek Prahlad
 */
public class SocketListener implements Runnable, ScriptListener {
    private int port;
    private FrankensteinRecorder recorder;
    protected Thread thread;
    protected ServerSocket socket;
    private boolean run = true;
    private boolean passed;

    public SocketListener(FrankensteinRecorder recorder) {
        this.recorder = recorder;
        recorder.addScriptListener(this);
    }

    public synchronized void start(int port) {
        this.port = port;
        try {
            socket = new ServerSocket(this.port);
        } catch (IOException e) {
            Logger.getLogger("Frankenstein").log(Level.WARNING, "Error with socket IO", e);
        }

        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            while (run) {
                Socket sock = null;
                try {
                    sock = socket.accept();
                } catch (IOException e) {
                    Logger.getLogger("Frankenstein").log(Level.WARNING, "Accept failed because Socket is closed or not bound.", e);
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
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
                sock.close();
            }
        } catch (IOException e) {
            Logger.getLogger("Frankenstein").log(Level.WARNING, "Error with socket IO", e);
        }
    }

    private void writeScriptCompletion(BufferedWriter writer) throws IOException {
        writer.write(passed ? Constants.SCRIPT_PASSED : Constants.SCRIPT_FAILED);
        writer.flush();
    }

    public synchronized void stop() {
        try {
            run = false;
            if (socket != null) socket.close();
            if (thread != null) thread.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            notifyAll();
        }
    }

    public synchronized void scriptCompleted(boolean passed) {
        this.passed = passed;
        notifyAll();
    }

    public void scriptStepStarted(int frankensteinEvent) {
    }
}

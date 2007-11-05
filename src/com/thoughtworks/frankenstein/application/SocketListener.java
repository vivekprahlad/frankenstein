package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.recorders.ScriptListener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listens for incoming requests from drivers.
 *
 * @author Vivek Prahlad
 */
public class SocketListener implements Runnable, ScriptListener {
    protected Thread thread;
    protected ServerSocket socket;
    private boolean run = true;
    private RequestProcessingStrategy requestProcessingStrategy;

    public SocketListener(RequestProcessingStrategy requestProcessingStrategy) {
        this.requestProcessingStrategy = requestProcessingStrategy;
    }

    public synchronized void start(int port) {
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            Logger.getLogger("Frankenstein").log(Level.WARNING, "Error with socket IO", e);
        }

        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            while (run) {
                Socket sock;
                try {
                    sock = socket.accept();
                } catch (IOException e) {
                    Logger.getLogger("Frankenstein").log(Level.WARNING, "Accept failed because Socket is closed or not bound.", e);
                    return;
                }
                requestProcessingStrategy.handleRequest(new BufferedReader(new InputStreamReader(sock.getInputStream())), new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));
                sock.close();
            }
        } catch (IOException e) {
            Logger.getLogger("Frankenstein").log(Level.WARNING, "Error with socket IO", e);
        }
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
        requestProcessingStrategy.completeRequest(passed);
    }

    public void scriptStepStarted(int frankensteinEvent) {
    }
}

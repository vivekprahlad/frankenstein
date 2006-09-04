package com.thoughtworks.frankenstein.application;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listens for incoming requests from drivers.
 * @author Vivek Prahlad
 */
public class SocketListener implements Runnable {
    private int port;
    private FrankensteinRecorder recorder;
    protected Thread thread;
    protected ServerSocket socket;
    private boolean run = true;

    public SocketListener(int port, FrankensteinRecorder recorder) {
        this.port = port;
        this.recorder = recorder;
    }

    public SocketListener(FrankensteinRecorder recorder) {
        this(5678, recorder);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        while(socket == null) {
            ThreadUtil.sleep(50);
        }
    }

    public void run() {
        try {
            socket = new ServerSocket(port);
            while (run) {
                Socket sock = socket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                recorder.load(reader);
                reader.close();
                sock.close();
                recorder.play();
            }
        } catch (IOException e) {
        }
    }

    public synchronized void stop() {
        try {
            run = false;
            if (socket!=null) socket.close();
            if (thread!=null) thread.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

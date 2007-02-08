package com.thoughtworks.frankenstein.application;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

    public SocketListener(int port, FrankensteinRecorder recorder) {
        this.port = port;
        this.recorder = recorder;
        recorder.addScriptListener(this);
    }

    public SocketListener(FrankensteinRecorder recorder) {
        this(5678, recorder);
    }

    public synchronized void start(int port) {
        this.port = port;
        thread = new Thread(this);
        thread.start();
        while (socket == null) {
            ThreadUtil.sleep(50);
        }
    }

    public void run() {
        try {
            socket = new ServerSocket(port);
            Socket sock;
            BufferedReader reader;
            while (run) {
                sock = socket.accept();
                reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                recorder.load(reader);
                recorder.play();
                synchronized (this) {
                    wait();//Wait for the script to complete
                }
                writeScriptCompletion(sock);
                reader.close();
                sock.close();
            }
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (InterruptedException e) {
        }
    }

    private void writeScriptCompletion(Socket sock) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        writer.write(passed ? "P" : "F");
        writer.close();
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
}

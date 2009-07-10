package com.thoughtworks.frankenstein.spikes;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.application.ThreadUtil;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;

/**
 *
 */
public class ProgressbarSpike {
    public static void main(String[] args) {
        DefaultWindowContext context = new DefaultWindowContext();
        createFrame("Title1");
//        createFrame("Title2");
        enumerateFrames();
    }

    private static void enumerateFrames() {
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            System.out.println("Frame: " + frames[i].getTitle());
        }
    }

    private static void createFrame(String title) {
        JFrame frame = new JFrame(title);
        JDesktopPane contentPane = new JDesktopPane();
        frame.setContentPane(contentPane);
        addWindow(frame);
        frame.setVisible(true);
    }

    private static void addWindow(final JFrame frame) {
        final ProgressMonitor monitor = new ProgressMonitor(frame, "foo", "bar", 0,100);
        Thread monitorThread = new Thread(new Runnable() {
            public void run() {
                for(int i=0; i<=100; i++) {
                    ThreadUtil.sleep(100);
                    monitor.setProgress(i);
                }
            }
        });
        monitorThread.start();
    }

}

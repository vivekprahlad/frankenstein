package com.thoughtworks.frankenstein.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;

public class ControlButtonPanel extends JPanel {
    private FrankensteinRecorder recorder;
    private FileDialogLauncher launcher;

    public ControlButtonPanel(FrankensteinRecorder recorder, FileDialogLauncher launcher) {
        this.recorder = recorder;
        this.launcher = launcher;
        createPanel();
    }

    private void createPanel() {
        add(startButton());
        add(stopButton());
        add(playButton());
        add(saveButton());
        add(loadButton());
        add(resetButton());
    }

    private JButton saveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.stop();
                launcher.launchSaveDialog(ControlButtonPanel.this, new JFileChooser("."), recorder);
            }
        });
        return saveButton;
    }

    private JButton loadButton() {
        JButton saveButton = new JButton("Load");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.stop();
                launcher.launchOpenDialog(ControlButtonPanel.this, new JFileChooser("."), recorder);
            }
        });
        return saveButton;
    }

    private JButton playButton() {
        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.play();
            }
        });
        return playButton;
    }

    private JButton resetButton() {
        JButton playButton = new JButton("Reset");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.reset();
            }
        });
        return playButton;
    }

    private JButton stopButton() {
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.stop();
            }
        });
        return stopButton;
    }

    private JButton startButton() {
        JButton start = new JButton("Record");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.start();
            }
        });
        return start;
    }
}

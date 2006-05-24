package com.thoughtworks.frankenstein.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.TableModel;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;

/**
 * User interface for recorded events.
 * @author Vivek Prahlad
 */
public class RecorderPane extends JPanel {
    private FrankensteinRecorder recorder;
    private FileDialogLauncher launcher;

    public RecorderPane(FrankensteinRecorder recorder, FileDialogLauncher launcher, TableModel tableModel) {
        this.recorder = recorder;
        this.launcher = launcher;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buttonPanel());
        add(new JScrollPane(new JTable(tableModel)));
    }

    private Component buttonPanel() {
        JPanel panel = new JPanel();
        panel.add(startButton());
        panel.add(stopButton());
        panel.add(playButton());
        panel.add(saveButton());
        panel.add(loadButton());
        panel.add(resetButton());
        return panel;
    }

    private JButton saveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.stop();
                launcher.launchSaveDialog(RecorderPane.this, new JFileChooser("."), recorder);
            }
        });
        return saveButton;
    }

    private JButton loadButton() {
        JButton saveButton = new JButton("Load");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recorder.stop();
                launcher.launchOpenDialog(RecorderPane.this, new JFileChooser("."), recorder);
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

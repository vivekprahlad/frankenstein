package com.thoughtworks.frankenstein.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;

public class ControlButtonPanel extends JPanel {
    private FrankensteinRecorder recorder;
    private FileDialogLauncher launcher;

    public ControlButtonPanel(final FrankensteinRecorder recorder, FileDialogLauncher launcher) {
        this.recorder = recorder;
        this.launcher = launcher;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (areControlKeysDown(event) && event.getID() == KeyEvent.KEY_RELEASED) {
                    handleKeyStrokes(event, recorder);
                }
                return false;
            }
        });
        createPanel();
    }

    private void handleKeyStrokes(KeyEvent event, FrankensteinRecorder recorder) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_R:
                recorder.start();
                break;
            case KeyEvent.VK_T:
                recorder.stop();
                break;
            case KeyEvent.VK_P:
                recorder.play();
                break;
            case KeyEvent.VK_S:
                save(recorder);
                break;
            case KeyEvent.VK_L:
                load(recorder);
                break;
            case KeyEvent.VK_E:
                recorder.reset();
                break;
        }
    }

    private void load(FrankensteinRecorder recorder) {
        try {
            recorder.load(new File(("recorded.script")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(FrankensteinRecorder recorder) {
        try {
            recorder.save(new File("recorded.script"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean areControlKeysDown(KeyEvent e) {
        return e.isControlDown() && e.isAltDown() && e.isShiftDown();
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

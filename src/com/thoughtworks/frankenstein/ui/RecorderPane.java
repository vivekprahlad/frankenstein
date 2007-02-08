package com.thoughtworks.frankenstein.ui;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;
import com.thoughtworks.frankenstein.playback.PlaybackSpeedControlScriptListener;

/**
 * User interface for recorded events.
 *
 * @author Vivek Prahlad
 */
public class RecorderPane extends JPanel {

    public RecorderPane(FrankensteinRecorder recorder, FileDialogLauncher launcher, RecorderTableModel tableModel, PlaybackSpeedControlScriptListener playbackSpeedControlScriptListener) {
        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.add(new ControlButtonPanel(recorder, launcher));
        controlPanel.add(new SpeedControlPanel(playbackSpeedControlScriptListener));

        add(controlPanel, BorderLayout.PAGE_START);
        DefaultRecorderTable recorderTable = new DefaultRecorderTable(tableModel);
        recorder.addScriptListener(new RowSelectionScriptListener(recorderTable));
        recorder.addScriptListener(playbackSpeedControlScriptListener);
        JScrollPane scrollPane = new JScrollPane(recorderTable);
        scrollPane.setAutoscrolls(true);
        add(scrollPane, BorderLayout.CENTER);
    }
}

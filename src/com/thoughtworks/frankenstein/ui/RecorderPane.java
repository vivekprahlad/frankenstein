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

    public RecorderPane(FrankensteinRecorder recorder, FileDialogLauncher launcher, RecorderTableModel tableModel,
                        PlaybackSpeedControlScriptListener listener) {
        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.add(new ControlButtonPanel(recorder, launcher));
        controlPanel.add(new SpeedControlPanel(listener));

        add(controlPanel, BorderLayout.PAGE_START);
        DefaultRecorderTable recorderTable = new DefaultRecorderTable(tableModel);
        if (tableModel!=null) {
            tableModel.setTable(recorderTable);
        }
        recorder.addScriptListener(new RowSelectionScriptListener(recorderTable));
        recorder.addScriptListener(listener);
        JScrollPane scrollPane = new JScrollPane(recorderTable);
        scrollPane.setAutoscrolls(true);
        add(scrollPane, BorderLayout.CENTER);
    }
}

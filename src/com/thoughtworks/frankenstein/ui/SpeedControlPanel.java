package com.thoughtworks.frankenstein.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.PlaybackSpeedControlScriptListener;

public class SpeedControlPanel extends JPanel {
    private PlaybackSpeedControlScriptListener speedControlListener;

    public SpeedControlPanel(PlaybackSpeedControlScriptListener listener) {
        this.speedControlListener = listener;
        JRadioButton slowButton = slowRadioButton();
        JRadioButton fastButton = fastRadioButton();
        createPanel(slowButton, fastButton);
        addButtonsToNewButtonGroup(slowButton, fastButton);
    }

    private void addButtonsToNewButtonGroup(JRadioButton slowButton, JRadioButton fastButton) {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(slowButton);
        buttonGroup.add(fastButton);
    }

    private void createPanel(JRadioButton slowButton, JRadioButton fastButton) {
        add(slowButton);
        add(fastButton);
    }

    private JRadioButton slowRadioButton() {
        JRadioButton radioButton = new JRadioButton("Slow");
        radioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                speedControlListener.setShouldWaitBetweenSteps(true);
            }
        });
        return radioButton;
    }

    private JRadioButton fastRadioButton() {
        JRadioButton radioButton = new JRadioButton("Fast");
        radioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                speedControlListener.setShouldWaitBetweenSteps(false);
            }
        });
        radioButton.setSelected(true);
        return radioButton;
    }
}

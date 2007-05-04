package com.thoughtworks.frankenstein.ui;

import javax.swing.*;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;

/**
 * Understands launching file dialogs
 *
 * @author Vivek Prahlad
 */
public interface FileDialogLauncher {

    void launchOpenDialog(JPanel pane, JFileChooser chooser, FrankensteinRecorder recorder);

    void launchSaveDialog(JPanel pane, JFileChooser chooser, FrankensteinRecorder recorder);
    
}

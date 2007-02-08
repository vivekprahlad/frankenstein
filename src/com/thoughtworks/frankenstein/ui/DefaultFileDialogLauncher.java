package com.thoughtworks.frankenstein.ui;

import java.io.IOException;
import javax.swing.*;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;

/**
 * Understands launching swing file choosers.
 *
 * @author Vivek Prahlad
 */
public class DefaultFileDialogLauncher implements FileDialogLauncher {

    public void launchOpenDialog(JPanel pane, JFileChooser chooser, FrankensteinRecorder recorder) {
        if (chooser.showOpenDialog(pane) == JFileChooser.APPROVE_OPTION) {
            try {
                recorder.load(chooser.getSelectedFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void launchSaveDialog(JPanel pane, JFileChooser chooser, FrankensteinRecorder recorder) {
        if (chooser.showSaveDialog(pane) == JFileChooser.APPROVE_OPTION) {
            try {
                recorder.save(chooser.getSelectedFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

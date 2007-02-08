package com.thoughtworks.frankenstein.ui;

import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;
import com.thoughtworks.frankenstein.playback.PlaybackSpeedControlScriptListener;

/**
 * Ensures behaviour of the RecorderPane
 */
public class RecorderPaneTest extends MockObjectTestCase {
    private Mock mockFrankensteinRecorder;
    private RecorderPane pane;
    private JFrame frame;
    private Mock mockFileDialogLauncher;

    protected void setUp() throws Exception {
        super.setUp();
        mockFrankensteinRecorder = mock(FrankensteinRecorder.class);
        mockFileDialogLauncher = mock(FileDialogLauncher.class);
        mockFrankensteinRecorder.expects(atLeastOnce()).method("addScriptListener");
        PlaybackSpeedControlScriptListener playbackSpeedControlScriptListener = new PlaybackSpeedControlScriptListener(100, false);
        pane = new RecorderPane((FrankensteinRecorder) mockFrankensteinRecorder.proxy(), (FileDialogLauncher) mockFileDialogLauncher.proxy(), null, playbackSpeedControlScriptListener);
        frame = new JFrame();
        frame.getContentPane().add(pane);
    }

    public void testDoNothing() {
    }
}

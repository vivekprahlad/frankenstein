package com.thoughtworks.frankenstein.ui;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.application.FrankensteinRecorder;

/**
 * Ensures behaviour of DefaultDialogLauncher
 */
public class DefaultFileDialogLauncherTest extends MockObjectTestCase {
    private File file;
    private DefaultFileDialogLauncher launcher;
    private Mock mockFrankensteinRecorder;
    private Mock mockFileDialogLauncher;
    private RecorderPane pane;

    protected void setUp() throws Exception {
        super.setUp();
        file = new File("abc");
        launcher = new DefaultFileDialogLauncher();
        mockFrankensteinRecorder = mock(FrankensteinRecorder.class);
        mockFrankensteinRecorder.expects(atLeastOnce()).method("addScriptListener");
        mockFileDialogLauncher = mock(FileDialogLauncher.class);
        pane = new RecorderPane((FrankensteinRecorder) mockFrankensteinRecorder.proxy(), (FileDialogLauncher) mockFileDialogLauncher.proxy(), null, null);
    }

    public void testLaunchesSaveFileDialog() {
        TestFileChooser chooser = new TestFileChooser(JFileChooser.APPROVE_OPTION, file);
        mockFrankensteinRecorder.expects(once()).method("save").with(eq(file));
        launcher.launchSaveDialog(pane, chooser, (FrankensteinRecorder) mockFrankensteinRecorder.proxy());
        assertTrue(chooser.showSaveCalled);
    }

    public void testLaunchesOpenFileDialog() {
        TestFileChooser chooser = new TestFileChooser(JFileChooser.APPROVE_OPTION, file);
        mockFrankensteinRecorder.expects(once()).method("load").with(eq(file));
        launcher.launchOpenDialog(pane, chooser, (FrankensteinRecorder) mockFrankensteinRecorder.proxy());
        assertTrue(chooser.showOpenCalled);
    }

    public void testThrowsExceptionIfUserSelectsOtherOptionWithSave() {
        TestFileChooser chooser = new TestFileChooser(JFileChooser.CANCEL_OPTION, file);
        launcher.launchSaveDialog(pane, chooser, (FrankensteinRecorder) mockFrankensteinRecorder.proxy());
    }

    public void testThrowsExceptionIfUserSelectsOtherOptionWithOpen() {
        TestFileChooser chooser = new TestFileChooser(JFileChooser.CANCEL_OPTION, file);
        launcher.launchOpenDialog(pane, chooser, (FrankensteinRecorder) mockFrankensteinRecorder.proxy());
    }

    public void testThrowsExceptionIfRecorderCannotSaveFile() {
        TestFileChooser chooser = new TestFileChooser(JFileChooser.APPROVE_OPTION, file);
        mockFrankensteinRecorder.expects(once()).method("save").with(eq(file)).will(throwException(new IOException()));
        try {
            launcher.launchSaveDialog(pane, chooser, (FrankensteinRecorder) mockFrankensteinRecorder.proxy());
            fail();
        } catch (RuntimeException e) {
        }
    }

    public void testThrowsExceptionIfRecorderCannotOpenFile() {
        TestFileChooser chooser = new TestFileChooser(JFileChooser.APPROVE_OPTION, file);
        mockFrankensteinRecorder.expects(once()).method("load").with(eq(file)).will(throwException(new IOException()));
        try {
            launcher.launchOpenDialog(pane, chooser, (FrankensteinRecorder) mockFrankensteinRecorder.proxy());
            fail();
        } catch (RuntimeException e) {
        }
    }

    private class TestFileChooser extends JFileChooser {
        private boolean showSaveCalled, showOpenCalled;
        private int returnValue;
        private File file;

        public TestFileChooser(int returnValue, File file) {
            this.returnValue = returnValue;
            this.file = file;
        }

        public int showSaveDialog(Component parent) throws HeadlessException {
            showSaveCalled = true;
            return returnValue;
        }

        public int showOpenDialog(Component parent) throws HeadlessException {
            showOpenCalled = true;
            return returnValue;
        }

        public File getSelectedFile() {
            return file;
        }
    }
}

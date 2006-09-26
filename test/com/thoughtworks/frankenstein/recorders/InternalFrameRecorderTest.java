package com.thoughtworks.frankenstein.recorders;

import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.InternalFrameShownEvent;
import com.thoughtworks.frankenstein.events.ActivateInternalFrameEvent;
import com.thoughtworks.frankenstein.events.CloseInternalFrameEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;

/**
 * Ensures behaviour of the JInternalFrame.
 */
public class InternalFrameRecorderTest extends AbstractRecorderTestCase {
    private InternalFrameRecorder recorder;

    protected void setUp() throws Exception {
        super.setUp();
        recorder = new InternalFrameRecorder((Recorder) mockRecorder.proxy(), new DefaultNamingStrategy());
    }

    public void testAddsInternalFrameListenerWhenFrameIsShown() {
        JInternalFrame internalFrame = new JInternalFrame("title");
        int initialListenerCount = internalFrameListenerCount(internalFrame);
        mockRecorder.expects(once()).method("record").with(eq(new InternalFrameShownEvent("title")));
        recorder.componentShown(internalFrame);
        assertEquals(initialListenerCount + 1, internalFrameListenerCount(internalFrame));
    }

    private int internalFrameListenerCount(JInternalFrame internalFrame) {
        return internalFrame.getInternalFrameListeners().length;
    }

    public void testRemovesInternalFrameListenerWhenFrameIsHidden() {
        JInternalFrame internalFrame = new JInternalFrame("title");
        int initialListenerCount = internalFrameListenerCount(internalFrame);
        mockRecorder.expects(once()).method("record").with(eq(new InternalFrameShownEvent("title")));
        recorder.componentShown(internalFrame);
        recorder.componentHidden(internalFrame);
        assertEquals(initialListenerCount, internalFrameListenerCount(internalFrame));
    }

    public void testRecordsFrameActivation() throws PropertyVetoException, InterruptedException, InvocationTargetException {
        final JInternalFrame internalFrame = createInternalFrame();
        int initialListenerCount = internalFrameListenerCount(internalFrame);
        mockRecorder.expects(once()).method("record").with(eq(new InternalFrameShownEvent("title")));
        recorder.componentShown(internalFrame);
        mockRecorder.expects(once()).method("record").with(eq(new ActivateInternalFrameEvent("title")));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    internalFrame.setSelected(true);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            }
        });
        waitForIdle();
    }

    private JInternalFrame createInternalFrame() throws InvocationTargetException, InterruptedException {
        final JInternalFrame[] internalFrame = new JInternalFrame[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                internalFrame[0] = new JInternalFrame("title") {
                    public boolean isShowing() {
                        return true;
                    }
                };
            }
        });
        return internalFrame[0];
    }

    public void testRecordsFrameClosing() throws PropertyVetoException, InterruptedException, InvocationTargetException {
        JInternalFrame internalFrame = createInternalFrame();
        int initialListenerCount = internalFrameListenerCount(internalFrame);
        mockRecorder.expects(once()).method("record").with(eq(new InternalFrameShownEvent("title")));
        recorder.componentShown(internalFrame);
        mockRecorder.expects(once()).method("record").with(eq(new CloseInternalFrameEvent("title")));
        internalFrame.setClosed(true);
    }
}

package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.InternalFrameShownEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording internal frame events.
 * @author Vivek Prahlad
 */
public class InternalFrameRecorder extends AbstractComponentRecorder {

    public InternalFrameRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JInternalFrame.class);
    }

    void componentShown(Component component) {
        recorder.record(new InternalFrameShownEvent("title"));
    }

    void componentHidden(Component component) {
    }
}

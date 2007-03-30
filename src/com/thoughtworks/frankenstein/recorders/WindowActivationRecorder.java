package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.ActivateWindowEvent;
import com.thoughtworks.frankenstein.naming.ComponentHierarchyWalker;
import com.thoughtworks.frankenstein.ui.RecorderPane;

/**
 * Understands recording window activation events.
 *
 * @author Vivek Prahlad
 */
public class WindowActivationRecorder implements ComponentRecorder, AWTEventListener {
    private EventRecorder recorder;
    public static final String JAVA_CONSOLE = "Java Console";

    public WindowActivationRecorder(EventRecorder recorder) {
        this.recorder = recorder;
    }

    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent event) {
        if (event instanceof WindowEvent) {
            WindowEvent windowEvent = (WindowEvent) event;
            if (windowEvent.getID() == WindowEvent.WINDOW_ACTIVATED) {
                if (windowEvent.getWindow() instanceof JFrame) {
                    JFrame frame = (JFrame) windowEvent.getWindow();
                    if (hasNoRecorderPane(frame) && frameNotJavaConsole(frame)) {
                        recorder.record(new ActivateWindowEvent(frame.getTitle()));
                    }

                }
            }
        }
    }

    private boolean frameNotJavaConsole(JFrame frame) {
        return !frame.getTitle().equalsIgnoreCase(JAVA_CONSOLE);
    }

    protected boolean hasNoRecorderPane(Container component) {
        return new ComponentHierarchyWalker().hasNoMatches(component, RecorderPane.class);
    }
}

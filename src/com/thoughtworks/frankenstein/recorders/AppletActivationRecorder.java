package com.thoughtworks.frankenstein.recorders;

import java.awt.event.AWTEventListener;
import java.awt.event.FocusEvent;
import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.events.ActivateAppletEvent;

/**
 * Understands recording Applet Activation.
 *
 * @author Pavan
 */
public class AppletActivationRecorder implements ComponentRecorder, AWTEventListener {
    private EventRecorder recorder;

    public AppletActivationRecorder(EventRecorder recorder) {
        this.recorder = recorder;
    }

    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.FOCUS_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);

    }

    public void eventDispatched(AWTEvent event) {
        if(event instanceof FocusEvent) {
            FocusEvent focusEvent = (FocusEvent) event;
            if(focusEvent.getID() == FocusEvent.FOCUS_GAINED) {
                if(focusEvent.getSource() instanceof JApplet) {
                    JApplet applet = (JApplet) focusEvent.getSource();
                    recorder.record(new ActivateAppletEvent(applet.getName()));
                }
            }
        }
    }
}

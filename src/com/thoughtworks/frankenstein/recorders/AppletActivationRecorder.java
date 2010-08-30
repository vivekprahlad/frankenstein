package com.thoughtworks.frankenstein.recorders;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;

import com.thoughtworks.frankenstein.application.FrankensteinApplet;
import com.thoughtworks.frankenstein.events.ActivateAppletEvent;

/**
 * Understands recording Applet Activation.
 *
 * @author Pavan
 * @author Nilesh
 */
public class AppletActivationRecorder implements ComponentRecorder, AWTEventListener {
    private EventRecorder recorder;

    public AppletActivationRecorder(EventRecorder recorder) {
        this.recorder = recorder;
    }

    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_FOCUS_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);

    }

    public void eventDispatched(AWTEvent event) {
        if (event instanceof WindowEvent) {
			WindowEvent windowEvent = (WindowEvent) event;
			if (windowEvent.getID() == WindowEvent.WINDOW_GAINED_FOCUS && event.getSource() instanceof Frame) {
				Frame frame = (Frame) event.getSource();
				Component compo[] = frame.getComponents();
                for (int i = 0; i < compo.length; i++) {
                    Component component = compo[i];
                    Container container = (Container) component;
                    Component components[] = container.getComponents();
                    if (components[0] instanceof FrankensteinApplet) {
                        FrankensteinApplet fApplet = (FrankensteinApplet) components[0];
                        Applet applet = fApplet.getAppletObject();
                        recorder.record(new ActivateAppletEvent(applet
                                .getName()));
                    }
                }
            }
		}
    }
}

package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.thoughtworks.frankenstein.application.FrankensteinApplet;
import com.thoughtworks.frankenstein.events.ActivateAppletEvent;

/**
 * Ensures the behaviour of recording JApplets.
 *
 * @author Pavan
 */
public class AppletActivationRecorderTest extends AbstractRecorderTestCase {
    private AppletActivationRecorder appletActivationRecorder;

    protected void setUp() throws Exception {
        super.setUp();
        appletActivationRecorder = new AppletActivationRecorder((Recorder)mockRecorder.proxy());
    }

    public void testListensForAppletGettingActivated() {
        mockRecorder.expects(once()).method("record").with(eq(new ActivateAppletEvent("testApplet")));
        Frame frame = new Frame();
        Container container = new Container();
        JApplet actualApplet = new JApplet();
        FrankensteinApplet frankensteinApplet = new FrankensteinApplet(actualApplet);
        actualApplet.setName("testApplet");
        frame.add(container);
        container.add(frankensteinApplet);
        appletActivationRecorder.eventDispatched(new WindowEvent(frame, WindowEvent.WINDOW_GAINED_FOCUS));
        frame.dispose();
    }
}

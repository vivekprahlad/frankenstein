package com.thoughtworks.frankenstein.recorders;

import java.awt.event.FocusEvent;

import javax.swing.*;

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
        JApplet jApplet = new JApplet();
        jApplet.setName("testApplet");
        appletActivationRecorder.eventDispatched(new FocusEvent(jApplet, FocusEvent.FOCUS_GAINED));
    }
}

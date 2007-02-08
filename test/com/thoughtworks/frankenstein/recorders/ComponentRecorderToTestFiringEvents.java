package com.thoughtworks.frankenstein.recorders;

import java.awt.*;

public class ComponentRecorderToTestFiringEvents extends AbstractComponentRecorder {

    public static int eventDispatchedCallCount = 0;

    public ComponentRecorderToTestFiringEvents() {
        super(null, null, null);
    }

    public void eventDispatched(AWTEvent event) {
        eventDispatchedCallCount++;
    }

    protected void componentShown(Component component) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void componentHidden(Component component) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

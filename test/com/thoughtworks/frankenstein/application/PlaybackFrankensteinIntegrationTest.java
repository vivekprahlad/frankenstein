package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.events.ActivateWindowEvent;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * Ensures the behaviour of PLaybackFrankensteinIntegration
 */
public class PlaybackFrankensteinIntegrationTest extends MockObjectTestCase {
    public void testActivateWindowEvent() {
        Mock scriptContextMock = mock(ScriptContext.class);
        PlaybackFrankensteinIntegration playbackFrankensteinIntegration = new PlaybackFrankensteinIntegration(Object.class);
        String windowTitle = "window";
        ActivateWindowEvent windowEvent = new ActivateWindowEvent(windowTitle);
        setScriptContextExpectation(windowEvent, scriptContextMock);
        playbackFrankensteinIntegration.setScriptContext((ScriptContext) scriptContextMock.proxy());
        playbackFrankensteinIntegration.play(windowEvent);
    }

    private void setScriptContextExpectation(FrankensteinEvent event, Mock mock) {
        mock.expects(once()).method("startMonitor");
        mock.expects(once()).method("play").with(eq(event));
    }
}
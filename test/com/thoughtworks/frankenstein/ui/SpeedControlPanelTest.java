package com.thoughtworks.frankenstein.ui;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.PlaybackSpeedControlScriptListener;
import junit.framework.TestCase;

public class SpeedControlPanelTest extends TestCase {
    private SpeedControlPanel panel;
    private PlaybackSpeedControlScriptListener speedControlListener;
    private UITestUtils uiTestUtils;

    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        speedControlListener = new PlaybackSpeedControlScriptListener(100, false);
        panel = new SpeedControlPanel(speedControlListener);
        uiTestUtils = new UITestUtils(panel);
    }

    public void testDefaultSpeedSettingIsFast() {
        assertFastSetting();
    }

    public void testAsksSlowPlaybackListenerToDelayBetweenStepsWhenSlowOptionIsSelected() {
        fastButton().doClick();
        slowButton().doClick();
        assertSlowSetting();
    }

    public void testAsksSlowPlaybackListenerNotToDelayBetweenStepsWhenFastOptionIsSelected() {
        slowButton().doClick();
        fastButton().doClick();
        assertFastSetting();
    }

    private void assertFastSetting() {
        assertFalse(speedControlListener.shouldWaitBetweenSteps());
        assertFalse(slowButton().isSelected());
        assertTrue(fastButton().isSelected());
    }

    private void assertSlowSetting() {
        assertTrue(speedControlListener.shouldWaitBetweenSteps());
        assertTrue(slowButton().isSelected());
        assertFalse(fastButton().isSelected());
    }

    private JRadioButton slowButton() {
        return uiTestUtils.getAndAssertRadioButton(0, "Slow");
    }

    private JRadioButton fastButton() {
        return uiTestUtils.getAndAssertRadioButton(1, "Fast");
    }
}

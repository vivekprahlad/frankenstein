package com.thoughtworks.frankenstein.playback;

import com.thoughtworks.frankenstein.application.ThreadUtil;
import com.thoughtworks.frankenstein.recorders.ScriptListener;

/**
 * Understands varying play back speed.
 *
 * @author Pavan
 * @author Prakash
 */
public class PlaybackSpeedControlScriptListener implements ScriptListener {
    private long milliseconds;
    private boolean shouldWait;

    public PlaybackSpeedControlScriptListener(long milliseconds, boolean shouldWait) {
        this.milliseconds = milliseconds;
        this.shouldWait = shouldWait;
    }

    public void scriptCompleted(boolean passed) {
    }

    public void scriptStepStarted(int eventIndex) {
        if (shouldWait) {
            ThreadUtil.sleep(milliseconds);
        }
    }

    public boolean shouldWaitBetweenSteps() {
        return shouldWait;
    }

    public void setShouldWaitBetweenSteps(boolean shouldWait) {
        this.shouldWait = shouldWait;
    }

    public void setDelay(int delayInMilliseconds) {
        milliseconds = delayInMilliseconds;
    }
}

package com.thoughtworks.frankenstein.playback;

import java.util.EventListener;

/**
 * Notifies interested listeners when a dialog is shown.
 *
 * @author vivek
 */
public interface WindowContextListener extends EventListener {
    public void dialogShown();
}

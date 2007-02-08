package com.thoughtworks.frankenstein.recorders;

import java.awt.*;

/**
 * Captures a screenshot into a specified folder.
 *
 * @author vivek
 */
public interface ScreenShot {
    String capture(String parentFolder, Robot robot);
}

package com.thoughtworks.frankenstein.recorders;

import java.awt.*;

/**
 * Understands SOMETHING
 *
 * @author vivek
 */
public interface ScreenShot {
    String capture(String parent, Robot robot);
}

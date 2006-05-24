package com.thoughtworks.frankenstein.recorders;

import java.awt.*;

/**
 * Default component visibility implementation
 * @author Vivek Prahlad
 */
public class DefaultComponentVisibility implements ComponentVisibility {
    public boolean isShowing(Component compnent) {
        return compnent.isShowing();
    }

    public boolean isShowingAndHasFocus(Component compnent) {
        return isShowing(compnent) && compnent.hasFocus();
    }
}

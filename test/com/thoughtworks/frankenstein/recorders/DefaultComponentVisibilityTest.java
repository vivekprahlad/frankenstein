package com.thoughtworks.frankenstein.recorders;

import junit.framework.TestCase;

import javax.swing.*;

/**
 * Ensures behaviour of DefaultComponentVisibility
 */
public class DefaultComponentVisibilityTest extends TestCase {

    public void testDefaultVisibility() {
        assertFalse(new DefaultComponentVisibility().isShowing(new JTextField()));
    }

    public void testDefaultVisibilityAndFocus() {
        assertFalse(new DefaultComponentVisibility().isShowingAndHasFocus(new JTextField()));
    }
}

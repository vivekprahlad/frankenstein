package com.thoughtworks.frankenstein.recorders;

import javax.swing.*;

import junit.framework.TestCase;

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

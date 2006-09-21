package com.thoughtworks.frankenstein.naming;

import junit.framework.TestCase;

import javax.swing.*;

/**
 * Ensures behaviour of button naming strategy
 */
public class ButtonNamingStrategyTest extends TestCase {

    public void testSimpleIconName() {
        assertEquals("resources/icons/Icon.gif", new ButtonNamingStrategy().simpleIconName("jar:/path/to/something/jar!/resources/icons/Icon.gif"));
    }

    public void testStripsSpacesFromButtonName() {
        JButton button = new JButton("Button Space");
        new ButtonNamingStrategy().name(button);
        assertEquals("ButtonSpace", button.getName());
    }
}

package com.thoughtworks.frankenstein.naming;

import javax.swing.*;

import junit.framework.TestCase;

/**
 * Ensures behaviour of button naming strategy
 */
public class ButtonNamingStrategyTest extends TestCase {

    public void testSimpleIconName() {
        assertEquals("Icon", new ButtonNamingStrategy("").simpleIconName("jar:/path/to/something/jar!/resources/icons/Icon.gif"));
    }

    public void testStripsSpacesFromButtonName() {
        JButton button = new JButton("Button Space");
        new ButtonNamingStrategy("").name(button, 1);
        assertEquals("ButtonSpace", button.getName());
    }

    public void testThrowsExceptionWhenUsedToNameOtherComponents() {
        try {
            new ButtonNamingStrategy("").name(new JTextField(), 1);
            fail("Should not be able to name text field");
        } catch (IllegalArgumentException exception) {
            //Expected
        }
    }
}

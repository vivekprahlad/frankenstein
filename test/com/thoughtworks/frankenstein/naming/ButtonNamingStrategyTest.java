package com.thoughtworks.frankenstein.naming;

import junit.framework.TestCase;

/**
 * Ensures behaviour of button naming strategy
 */
public class ButtonNamingStrategyTest extends TestCase {

    public void testSimpleIconName() {
        assertEquals("resources/icons/Icon.gif", new ButtonNamingStrategy().simpleIconName("jar:/path/to/something/jar!/resources/icons/Icon.gif"));
    }
}

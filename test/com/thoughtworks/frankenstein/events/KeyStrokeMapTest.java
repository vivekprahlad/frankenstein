package com.thoughtworks.frankenstein.events;

import java.awt.event.KeyEvent;

import junit.framework.TestCase;

/**
 * Ensures the functionality of KeyStrokeMap
 */
public class KeyStrokeMapTest extends TestCase {

    public void testGetKeyCodeFromTextReturnsCorrectKeyCode() {
        assertEquals(KeyEvent.VK_ALT, KeyStrokeMap.getKeyCodeFromText("Alt"));
        assertEquals(KeyEvent.VK_A, KeyStrokeMap.getKeyCodeFromText("A"));
    }

    public void testKeyCodeRetrievalShouldBeCaseInsensitive() {
        assertEquals(KeyEvent.VK_ALT, KeyStrokeMap.getKeyCodeFromText("ALT"));
        assertEquals(KeyEvent.VK_A, KeyStrokeMap.getKeyCodeFromText("a"));
    }

    public void testGetKeyCodeFromTextForKeysWithUnderscoreInKeyText() {
        assertEquals(KeyEvent.VK_PAGE_UP, KeyStrokeMap.getKeyCodeFromText("Page_Up"));
    }

    public void testGetKeyCodeFromTextForKeysWithSpaceInKeyText() {
        assertEquals(KeyEvent.VK_PAGE_UP, KeyStrokeMap.getKeyCodeFromText("Page Up"));
    }

    public void testGetKeyCodeFromTextForKeysWithSpaceInKeyAndCapitalizedText() {
        assertEquals(KeyEvent.VK_PAGE_UP, KeyStrokeMap.getKeyCodeFromText("PAGE UP"));
    }

    public void testGetKeyCodeFromTextForNonNumPadArrowKeys() {
        assertEquals(KeyEvent.VK_DOWN, KeyStrokeMap.getKeyCodeFromText("Down"));
    }

    public void testGetKeyCodeFromTextThrowsExceptionWhenCodeIsNotFound() {
        String keyText = "NOT_EXISTING_KEY_CODE_TEXT";
        try {
            KeyStrokeMap.getKeyCodeFromText(keyText);
            fail("RuntimeException was expected to be thrown.");
        } catch (RuntimeException ex) {
            String exceptionMessage = "KeyCode not found for the given KeyCode text " + keyText;
            assertTrue(ex.getMessage().equals(exceptionMessage));
        }
    }

    public void testGetModifiersFromTextReturnsCorrectModifier() {
        assertEquals(KeyEvent.ALT_DOWN_MASK, KeyStrokeMap.getModifiersFromText("Alt"));
    }

    public void testGetModifiersFromTextIsCaseInsensitive() {
        assertEquals(KeyEvent.ALT_DOWN_MASK, KeyStrokeMap.getModifiersFromText("AlT"));
    }

    public void testGetModifiersFromTextThrowsExceptionWhenModifierIsNotFound() {
        String modifierText = "NOT_EXISTING_MODIFIER_TEXT";
        try {
            KeyStrokeMap.getModifiersFromText(modifierText);
            fail("RuntimeException was expected to be thrown.");
        } catch (RuntimeException ex) {
            String exceptionMessage = "Modifier not found for the given Modifier text " + modifierText;
            assertTrue(ex.getMessage().equals(exceptionMessage));
        }
    }

    public void testGetModifiersExText() {
        assertEquals("Alt", KeyEvent.getModifiersExText(KeyEvent.ALT_DOWN_MASK));
    }

    public void testGetKeyTextForKeysWithoutSpaceInKeyText() {
        assertEquals("A", KeyStrokeMap.getKeyText(KeyEvent.VK_A));
    }

    public void testGetKeyTextForKeysWithSpaceInKeyText() {
        assertEquals("Page_Up", KeyStrokeMap.getKeyText(KeyEvent.VK_PAGE_UP));
    }

    public void testGetTextForNonNumPadArrowKeys() {
        assertEquals("Down", KeyStrokeMap.getKeyText(KeyEvent.VK_DOWN));
    }

    public void testGetTextForNumPadArrowKeys() {
        assertEquals("Down", KeyStrokeMap.getKeyText(KeyEvent.VK_KP_DOWN));
    }
}

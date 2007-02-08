package com.thoughtworks.frankenstein.events;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.frankenstein.common.Constants;

/**
 * The maping functionality for the Key codes with corresponding key text
 */
public class KeyStrokeMap {
    private static final Map KEY_MODIFIER_MAP = new HashMap();
    private static final Map KEY_TEXT_TO_KEY_CODE_MAP = new HashMap();

    static {
        initializeKeyModifierMap(KEY_MODIFIER_MAP);
        initializeKeyCodeMap(KEY_TEXT_TO_KEY_CODE_MAP);
    }

    static int getModifiersFromText(String modifierText) {
        int modifierCode = 0;
        String individualModifier[] = modifierText.split("\\+");

        for (int i = 0; i < individualModifier.length; i++) {
            Object code = KEY_MODIFIER_MAP.get(individualModifier[i]);
            if (code == null) {
                throw new RuntimeException("Modifier not found for the given Modifier text " + modifierText);
            }
            modifierCode |= ((Integer) code).intValue();
        }
        return modifierCode;
    }

    static int getKeyCodeFromText(String keyText) {
        Object keyCode = KEY_TEXT_TO_KEY_CODE_MAP.get(keyText);

        if (keyCode == null) {
            throw new RuntimeException("KeyCode not found for the given KeyCode text " + keyText);
        }

        return ((Integer) keyCode).intValue();
    }


    private static void initializeKeyCodeMap(Map keyTextToKeyCodeMap) {
        Field[] fields = KeyEvent.class.getFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String keyName = field.getName();
            if (keyName.startsWith("VK_") && !keyName.startsWith("VK_KP_")) {
                try {
                    int keyCode = field.getInt(null);
                    String keyText = KeyEvent.getKeyText(keyCode).replaceAll(Constants.SPACE, Constants.UNDERSCORE);
                    keyTextToKeyCodeMap.put(keyText, new Integer(keyCode));
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    private static void initializeKeyModifierMap(Map keyModifierMap) {
        Field[] fields = InputEvent.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getName().endsWith("_MASK")) {
                try {
                    int modifiers = field.getInt(null);
                    keyModifierMap.put(KeyEvent.getModifiersExText(modifiers), new Integer(modifiers));
                } catch (IllegalAccessException e) {
                }

            }
        }
    }

    static String getModifiersExText(int modifiers) {
        String modifiersText = KeyEvent.getModifiersExText(modifiers);
        return modifiersText;
    }

    static String getKeyText(int keyCode) {
        return KeyEvent.getKeyText(keyCode).replaceAll(Constants.SPACE, Constants.UNDERSCORE);
    }
}
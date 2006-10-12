package com.thoughtworks.frankenstein.common;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;

/**
 * Ensures behaviour of default renderer decoder.
 */
public class DefaultRendererDecoderTest extends TestCase {
    private DefaultComponentDecoder decoder;

    protected void setUp() throws Exception {
        decoder = new DefaultComponentDecoder();
    }

    public void testDecodesTextField() {
        assertEquals("abc", decoder.decode(new JTextField("abc")));
    }

    public void testDecodesJLabel() {
        assertEquals("abc", decoder.decode(new JLabel("abc")));
    }

    public void testDecodesJTextArea() {
        assertEquals("abc", decoder.decode(new JTextArea("abc")));
    }

    public void testDecodesJCheckBox() {
        assertEquals("true", decoder.decode(new JCheckBox("label", true)));
        assertEquals("false", decoder.decode(new JCheckBox("label", false)));
    }

    public void testDecodeUnregisteredDecoder() {
        assertEquals("Could not decode component of type: javax.swing.JRadioButton", decoder.decode(new JRadioButton("test")));
    }

    public void testRegisterDecoder() {
        decoder.registerDecoder(JRadioButton.class, new ComponentDecoder() {
            public String decode(Component renderer) {
                return ((JRadioButton) renderer).getText();
            }
        });
        assertEquals("test", decoder.decode(new JRadioButton("test")));
    }

}

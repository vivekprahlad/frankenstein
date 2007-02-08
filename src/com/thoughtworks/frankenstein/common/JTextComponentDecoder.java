package com.thoughtworks.frankenstein.common;

import java.awt.*;
import javax.swing.text.JTextComponent;

/**
 * Understands decoding JTextComponents
 *
 * @author Vivek Prahlad
 */
public class JTextComponentDecoder implements ComponentDecoder {
    public String decode(Component renderer) {
        return ((JTextComponent) renderer).getText();
    }
}

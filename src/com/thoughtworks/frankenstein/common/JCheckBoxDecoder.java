package com.thoughtworks.frankenstein.common;

import java.awt.*;
import javax.swing.*;

/**
 * Decodes text from JCheckBoxes.
 * @author Vivek Prahlad
 */
public class JCheckBoxDecoder implements ComponentDecoder {

    public String decode(Component renderer) {
        return Boolean.toString(((JCheckBox) renderer).isSelected());
    }
}

package com.thoughtworks.frankenstein.common;

import java.awt.*;
import javax.swing.*;

/**
 * Decodes JCheckBoxes.
 * @author Vivek Prahlad
 */
public class JCheckBoxDecoder implements RendererDecoder {

    public String decode(Component renderer) {
        return Boolean.toString(((JCheckBox) renderer).isSelected());
    }
}

package com.thoughtworks.frankenstein.common;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Decodes default swing components.
 * @author Vivek Prahlad
 */
public class DefaultRendererDecoder implements RendererDecoder {
    private Map componentDecoderMap;
    private RendererDecoder textComponentDecoder = new JTextComponentDecoder();
    private RendererDecoder jlabelDecoder = new JLabelDecoder();

    public DefaultRendererDecoder() {
        componentDecoderMap = new HashMap();
        componentDecoderMap.put(JCheckBox.class, new JCheckBoxDecoder());
    }

    public String decode(Component renderer) {
        if (renderer instanceof JTextComponent) return textComponentDecoder.decode(renderer);
        if (renderer instanceof JLabel) return jlabelDecoder.decode(renderer);
        if (componentDecoderMap.containsKey(renderer.getClass())) {
            RendererDecoder decoder = (RendererDecoder) componentDecoderMap.get(renderer.getClass());
            return decoder.decode(renderer);
        } else {
            return "Could not decode component of type: " + renderer.getClass().getName();
        }
    }

    public void registerDecoder(Class componentClass, RendererDecoder decoder) {
        componentDecoderMap.put(componentClass, decoder);
    }
}

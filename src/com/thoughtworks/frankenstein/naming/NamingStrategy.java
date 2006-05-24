package com.thoughtworks.frankenstein.naming;

import java.awt.*;

/**
 * Understands naming components.
 * @author Vivek Prahlad
 */
public interface NamingStrategy {
    void nameComponentsIn(Container panel);
}

package com.thoughtworks.frankenstein.naming;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Understands naming components.
 * @author Vivek Prahlad
 */
public interface NamingStrategy {
    void nameComponentsIn(Container panel);

    void nameComponentsIn(java.util.List components, ComponentNamingStrategy strategy, int counter);

    void nameComponentsIn(String prefix, Container component);
}

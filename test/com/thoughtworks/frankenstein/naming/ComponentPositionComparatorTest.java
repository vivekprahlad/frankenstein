package com.thoughtworks.frankenstein.naming;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Ensures behaviour of components positions.
 */
public class ComponentPositionComparatorTest extends TestCase {
    public void testSortsComponentsFromLeftToRightAndTopToBottom() {
        JTextField fieldOne  = new JTextField();
        JTextField fieldTwo  = new JTextField();
        JTextField fieldThree  = new JTextField();
        JTextField fieldFour  = new JTextField();
        addComponentsToFrame(fieldOne, fieldTwo, fieldThree, fieldFour);
        java.util.List list = addFields(fieldOne, fieldTwo, fieldThree, fieldFour);
        Collections.sort(list, new ComponentPositionComparator());
        assertEquals(fieldOne, list.get(0));
        assertEquals(fieldTwo, list.get(1));
        assertEquals(fieldThree, list.get(2));
        assertEquals(fieldFour, list.get(3));
    }

    private void addComponentsToFrame(JTextField fieldOne, JTextField fieldTwo, JTextField fieldThree, JTextField fieldFour) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(2,2));
        frame.getContentPane().add(fieldOne);
        frame.getContentPane().add(fieldTwo);
        frame.getContentPane().add(fieldThree);
        frame.getContentPane().add(fieldFour);
        frame.pack();
    }

    private java.util.List addFields(JTextField fieldOne, JTextField fieldTwo, JTextField fieldThree, JTextField fieldFour) {
        java.util.List list = new ArrayList();
        list.add(fieldThree);
        list.add(fieldFour);
        list.add(fieldOne);
        list.add(fieldTwo);
        return list;
    }
}

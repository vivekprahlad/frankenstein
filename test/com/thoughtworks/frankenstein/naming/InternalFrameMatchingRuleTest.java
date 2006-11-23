package com.thoughtworks.frankenstein.naming;

import junit.framework.TestCase;

import javax.swing.*;

/**
 * Ensures behaviour of InternalFrameMatchingRule
 * @author vivek
 */
public class InternalFrameMatchingRuleTest extends TestCase {

    public void testMatchesInternalFrame() {
        JInternalFrame frame = new JInternalFrame("Test Title");
        InternalFrameMatchingRule rule = new InternalFrameMatchingRule("Test Title");
        assertFalse(rule.hasMatches());
        assertFalse(rule.matchAndContinue(frame));
        assertTrue(rule.hasMatches());
    }
}

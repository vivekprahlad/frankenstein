package com.thoughtworks.frankenstein.application;

import spin.over.CheckingRepaintManager;
import spin.over.EDTRuleViolation;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Logs violations of Swing's single threaded rendering rule.
 * @author vivek
 */
public class EDTRuleViolationLoggingRepaintManager extends CheckingRepaintManager {
    protected void indicate(EDTRuleViolation edtRuleViolation) throws EDTRuleViolation {
        Logger.getLogger("Frankenstein").log(Level.WARNING, "", edtRuleViolation);
    }
}

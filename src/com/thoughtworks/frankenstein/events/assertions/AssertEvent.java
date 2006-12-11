package com.thoughtworks.frankenstein.events.assertions;

import com.thoughtworks.frankenstein.events.AbstractFrankensteinEvent;
import com.thoughtworks.frankenstein.playback.MatchStrategy;
import ognl.OgnlContext;
import ognl.OgnlException;

import java.awt.*;


/**
 * Understands evaluating arbitrary OGNL expressions
 *
 * @author vivek
 */
public class AssertEvent extends AbstractFrankensteinEvent {
    private String componentName;
    private String expressionString;
    private String expectedValue;
    private OgnlExpression expression;
    private static final OgnlContext OGNL_CONTEXT = new OgnlContext();

    public AssertEvent(String componentName, String expressionString, String expectedValue) {
        this.componentName = componentName;
        this.expressionString = expressionString;
        this.expression = new OgnlExpression(expressionString);
        this.expectedValue = expectedValue;
    }

    public AssertEvent(String scriptLine) {
        this(params(scriptLine)[0], colonSplit(scriptLine, 0), params(scriptLine)[1].substring(colonSplit(scriptLine, 0).length()+1,params(scriptLine)[1].length()));
    }

    private static String colonSplit(String script, int index) {
        return params(script)[1].split(":")[index];
    }

    public String toString() {
        return "AssertEvent: " + componentName + ", " + expressionString + ": " + expectedValue;
    }

    public String target() {
        return componentName;
    }

    public String parameters() {
        return expressionString + ":" + expectedValue;
    }

    public void run() {
        try {
            String actual = expression.getValue(OGNL_CONTEXT, finder.findComponent(context, componentName));
            if (!MatchStrategy.matchValues(actual,expectedValue)) {
                throw new RuntimeException("Expected: " + expectedValue + ", but was: " + actual);
            }
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
    }
}

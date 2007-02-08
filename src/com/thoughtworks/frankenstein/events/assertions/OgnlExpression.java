package com.thoughtworks.frankenstein.events.assertions;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

/**
 * Compiles OGNL expressions.
 *
 * @author vivek
 * @see AssertEvent
 */
public class OgnlExpression {
    private Object expression;
    private String expressionString;

    public OgnlExpression(String expressionString) {
        this.expressionString = expressionString;
        try {
            expression = Ognl.parseExpression(expressionString);
        } catch (OgnlException e) {
            throw new RuntimeException("Error parsing expression: ", e);
        }
    }

    public String getValue(OgnlContext context, Object rootObject) throws OgnlException {
        return String.valueOf(Ognl.getValue(expression, context, rootObject));
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj.getClass() == getClass())) return false;
        if (this == obj) return true;
        OgnlExpression other = (OgnlExpression) obj;
        return expressionString.equals(other.expressionString);
    }

    public int hashCode() {
        return expressionString.hashCode();
    }
}

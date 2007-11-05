package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.script.Script;

/**
 * Converts events to ruby code.
 */
public class RubyScriptStrategy implements ScriptStrategy {
    public static final String SPACE = " ";

    public String convert(String[] scriptElements) {
        return underscore(scriptElements[0]) + target(scriptElements[1]) + parameters(hasTarget(scriptElements[1]),scriptElements[2]);
    }

    private boolean hasTarget(String target) {
        return (!"".equals(target));
    }

    private String target(String element) {
        if (!hasTarget(element)) return "";
        return SPACE + quote(element);
    }

    private String parameters(boolean hasTarget, String element) {
        if ("".equals(element)) return "";
        return (hasTarget ? " , " : SPACE) + quote(Script.escapeNewLines(element)).replaceAll("\\s", SPACE).trim();
    }

    protected String underscore(String action) {
        return action.replaceAll("(\\w)([A-Z])", "$1_$2").toLowerCase();
    }

    protected String quote(String input) {
        return "\"" + input + "\"";
    }
}

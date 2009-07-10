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

    public String toMethod(String action) {
        return underscore(action);
    }

    public String enclose(String body) {
        return SPACE + body;
    }

    public String escape(boolean value) {
        return quote(String.valueOf(value));
    }

    public String escape(int value) {
        return quote(String.valueOf(value));
    }

    public String array(String[] strings) {
        return quote(toArray(strings));
    }

    public String array(int[] rows) {
        String array = "";
        for (int i = 0; i < rows.length; i++) {
            array += rows[i] + ",";
        }
        return rows.length == 0 ? "" : quote(array.substring(0, array.length()-1));        
    }

    public String cell(int row, int column) {
        return quote(String.valueOf(row) + "," + String.valueOf(column));
    }

    private String toArray(String[] strings) {
        String array = "";
        for (int i = 0; i < strings.length; i++) {
            array += strings[i] + ",";
        }
        return strings.length == 0 ? "" : array.substring(0, array.length()-1);
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

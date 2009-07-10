package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.script.Script;

public class JavaScriptStrategy implements ScriptStrategy {
    public static final String SPACE = " ";

    public String convert(String scriptElements[]) {
        return (new StringBuilder()).append(camelCase(scriptElements[0]))
                                    .append("(")
                                    .append(target(scriptElements[1]))
                                    .append(parameters(hasTarget(scriptElements[1]), scriptElements[2]))
                                    .append(")")
                                    .toString();
    }

    private String camelCase(String action) {
        return (new StringBuilder()).append(Character.toLowerCase(action.charAt(0)))
                                    .append(action.substring(1))
                                    .toString();
    }

    private boolean hasTarget(String target) {
        return !"".equals(target);
    }

    private String target(String element) {
        return !hasTarget(element) ? "" : (new StringBuilder()).append(" ").append(quote(element)).toString();
    }

    private String parameters(boolean hasTarget, String element) {
        return "".equals(element) ? "" : (new StringBuilder()).append(hasTarget ? " , " : " ")
                                        .append(quote(Script.escapeNewLines(element)).replaceAll("\\s", " ").trim()).toString();
    }

    protected String quote(String input) {
        return (new StringBuilder()).append("\"").append(input).append("\"").toString();
    }

}
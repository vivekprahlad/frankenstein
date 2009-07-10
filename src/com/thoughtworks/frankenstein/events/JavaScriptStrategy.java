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

    public String toMethod(String action) {
        return camelCase(action);
    }

    public String enclose(String body) {
        return "(" + body + ")";
    }

    public String escape(boolean value) {
        return String.valueOf(value);
    }

    public String escape(int value) {
        return String.valueOf(value);
    }

    public String array(String[] strings) {
        return "new String[]{" + toArray(strings) + "}";
    }

    public String array(int[] rows) {
        return "new int[]{" + toArray(rows) + "}";
    }

    public String cell(int row, int column) {
        return String.valueOf(row) + "," + String.valueOf(column);
    }

    private String toArray(int[] rows) {
        String array = "";
        for (int i = 0; i < rows.length; i++) {
            array += rows[i] + ",";
        }
        return rows.length == 0 ? "" : array.substring(0, array.length()-1);
    }

    private String toArray(String[] strings) {
        String array = "";
        for (int i = 0; i < strings.length; i++) {
            array += quote(strings[i]) + ",";
        }
        return strings.length == 0 ? "" : array.substring(0, array.length()-1);
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
        return !hasTarget(element) ? "" : (new StringBuilder()).append(quote(element)).toString();
    }

    private String parameters(boolean hasTarget, String element) {
        return "".equals(element) ? "" : (new StringBuilder()).append(hasTarget ? " , " : "")
                                        .append(quote(Script.escapeNewLines(element)).replaceAll("\\s", " ").trim()).toString();
    }

    protected String quote(String input) {
        return (new StringBuilder()).append("\"").append(input).append("\"").toString();
    }

}
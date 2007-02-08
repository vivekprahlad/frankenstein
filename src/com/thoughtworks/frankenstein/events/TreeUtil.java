package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.script.Script;

public class TreeUtil {
    static String name(String scriptLine) {
        return scriptLine.split(",")[0];
    }

    static String[] path(String scriptLine) {
        String[] line = scriptLine.split(",");
        String[] path = new String[line.length - 1];
        System.arraycopy(line, 1, path, 0, line.length - 1);
        for (int indexOfPath = 0; indexOfPath < path.length; indexOfPath++) {
            path[indexOfPath] = Script.unescapeSpecialCharacters(Script.unescapeNewLines(path[indexOfPath]));
        }
        return path;
    }

    public static String pathString(String[] path, String delimiter, String surrounding) {
        String p = "";
        for (int i = 0; i < path.length; i++) {
            p += surrounding + Script.escapeSpecialCharacters(Script.escapeNewLines(path[i])) + surrounding + delimiter;
        }
        return p.substring(0, p.length() - 1);
    }

    public static String pathString(String[] path, String delimiter) {
        return pathString(path, delimiter, "");
    }
}

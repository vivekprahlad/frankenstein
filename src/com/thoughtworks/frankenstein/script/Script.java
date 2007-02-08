package com.thoughtworks.frankenstein.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.frankenstein.application.ScriptReader;
import com.thoughtworks.frankenstein.events.EventRegistry;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Understands saving and restoring scripts.
 *
 * @author Vivek Prahlad
 */
public class Script {
    private EventRegistry registry;
    private static final String NEW_LINE = "&#xA;";
    private static final String COMMA = "&#x83;";

    public Script(EventRegistry registry) {
        this.registry = registry;
    }

    public String scriptText(List eventList) {
        String script = "";
        for (Iterator iterator = eventList.iterator(); iterator.hasNext();) {
            script += ((FrankensteinEvent) iterator.next()).scriptLine() + "\n";
        }
        return script.substring(0, script.length() - 1);
    }

    public List parse(Reader script) throws IOException {
        String[] lines = read(script).split("\n");
        List eventList = new ArrayList();
        for (int i = 0; i < lines.length; i++) {
            eventList.add(registry.createEvent(lines[i].trim()));
        }
        return eventList;
    }

    private String read(Reader reader) throws IOException {
        ScriptReader br = new ScriptReader(new BufferedReader(reader));
        String nextLine;
        StringBuffer sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null) {
            sb.append(nextLine).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    public static String escapeNewLines(String string) {
        return string.replaceAll("\n", NEW_LINE);
    }

    public static String unescapeNewLines(String rawLine) {
        return rawLine.replaceAll(NEW_LINE, "\n");
    }

    public static String escapeSpecialCharacters(String string) {
        return string.replaceAll(",", COMMA);
    }

    public static String unescapeSpecialCharacters(String rawLine) {
        return rawLine.replaceAll(COMMA, ",");
    }

}

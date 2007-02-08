package com.thoughtworks.frankenstein.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.thoughtworks.frankenstein.common.Constants;

/**
 * Encapsulates the protocol logic to read a script from a driver
 */
public class ScriptReader extends BufferedReader {

    private String line;

    public ScriptReader(Reader reader) {
        super(reader);
    }

    public String readLine() throws IOException {
        line = super.readLine();
        if (line != null && line.equals(Constants.END_OF_SCRIPT)) {
            return null;
        }
        return line;
    }
}

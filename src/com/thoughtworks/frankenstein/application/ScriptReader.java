package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.common.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Encapsulates the protocol logic to read a script from a driver
 */
public class ScriptReader extends BufferedReader {

    public ScriptReader(Reader reader) {
        super(reader);
    }

    public String readLine() throws IOException {
        String line = super.readLine();
        if (line != null && line.equals(Constants.END_OF_SCRIPT)) {
            return null;
        }
        return line;
    }
}

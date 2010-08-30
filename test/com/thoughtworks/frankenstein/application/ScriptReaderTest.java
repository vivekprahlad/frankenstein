package com.thoughtworks.frankenstein.application;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.thoughtworks.frankenstein.common.Constants;
import junit.framework.TestCase;

/**
 * Ensures the functionality of ScriptReader
 */
public class ScriptReaderTest extends TestCase {

    public void testScriptReaderReturnsNullWhenItReceivesENDOFSCRIPT() throws IOException {
        Reader reader = new StringReader(Constants.END_OF_SCRIPT + "\n");
        assertNull(new ScriptReader(reader).readLine());
    }

    public void testScriptReaderReturnsReadValueWhenItDoesNotRecieveENDOFSCRIPT() throws IOException {
        Reader reader = new StringReader("Hello World" + "\n");
        assertTrue(new ScriptReader(reader).readLine() != null);
    }
}

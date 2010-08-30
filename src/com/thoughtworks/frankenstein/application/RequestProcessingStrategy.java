package com.thoughtworks.frankenstein.application;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * This class represents a strategy for processing incoming script lines
 */
public interface RequestProcessingStrategy {
    
    void handleRequest(Reader reader, Writer writer) throws IOException;

    void completeRequest(boolean passed);
}

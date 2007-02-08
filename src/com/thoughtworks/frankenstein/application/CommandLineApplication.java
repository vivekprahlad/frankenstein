package com.thoughtworks.frankenstein.application;

/**
 * Represents an application launched from the Command Line.
 */
public class CommandLineApplication implements Application {
    private Class mainClass;

    public CommandLineApplication(Class mainClass) {
        this.mainClass = mainClass;
    }

    public void launch(String[] args) {
        try {
            mainClass.getDeclaredMethod("main", new Class[] {String[].class}).invoke(null, new Object[] {args});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

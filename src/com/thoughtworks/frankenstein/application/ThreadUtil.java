package com.thoughtworks.frankenstein.application;

/**
 * Thread utility methods
 *
 * @author Vivek Prahlad
 */
public class ThreadUtil {
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

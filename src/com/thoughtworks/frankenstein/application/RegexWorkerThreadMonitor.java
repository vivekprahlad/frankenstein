package com.thoughtworks.frankenstein.application;

import java.util.regex.Pattern;

/**
 * Monitors threads whose names match a supplied Regular Expression
 */
public class RegexWorkerThreadMonitor implements WorkerThreadMonitor {
    private final Pattern pattern;

    public RegexWorkerThreadMonitor(String regex) {
        pattern = Pattern.compile(".*" + regex + ".*");
    }

    public void start() {
        //No op.
    }

    public void waitForIdle() {
        while (hasActiveWorkerThreads()) {
            ThreadUtil.sleep(100);
        }
    }

    private boolean hasActiveWorkerThreads() {
        Thread[] threads = enumerateThreads();
        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            if(thread!=null && thread.isAlive()) {
                if (matchesPattern(thread.getName())) return true;
            }
        }
        return false;
    }

    private Thread[] enumerateThreads() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads;
        synchronized (threadGroup) {
            threads = new Thread[threadGroup.activeCount()];
            threadGroup.enumerate(threads, true);
        }
        return threads;
    }

    private boolean matchesPattern(String name) {
        return pattern.matcher(name).matches();
    }
}

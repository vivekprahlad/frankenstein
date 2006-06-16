package com.thoughtworks.frankenstein.application;

/**
 * Pipes to a designated main class.
 */
public class PipingMain {
    public static void main(String[] args) throws ClassNotFoundException {
        Class mainClass = parseClass(args);
        String[] mainClassArgs = parseArgs(args);
        FrankensteinIntegration integration = new FrankensteinIntegration(mainClass);
        integration.start(mainClassArgs);
    }

    static String[] parseArgs(String[] args) {
        String[] dest = new String[args.length-1];
        System.arraycopy(args, 1, dest, 0, args.length-1);
        return dest;
    }

    static Class parseClass(String[] args) throws ClassNotFoundException {
        return Class.forName(args[0]);
    }
}

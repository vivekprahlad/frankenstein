package com.thoughtworks.frankenstein.application;

/**
 * Launches a designated main class after launching Frankenstein.
 */
public class PipingMain {
    public static void main(String[] args) throws ClassNotFoundException {
        createFrankensteinIntegration(args).start(parseArgs(args));
    }

    protected static FrankensteinIntegrationIf createFrankensteinIntegration(String[] args) throws ClassNotFoundException {
        return FrankensteinIntegrationFactory.getFrankensteinIntegration(parseClass(args));
    }

    static String[] parseArgs(String[] args) {
        String[] dest = new String[args.length - 1];
        System.arraycopy(args, 1, dest, 0, args.length - 1);
        return dest;
    }

    static Class parseClass(String[] args) throws ClassNotFoundException {
        return Class.forName(args[0]);
    }
}

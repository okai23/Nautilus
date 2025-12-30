package org.ok23.Nautilus.Logging;

public class Logger
{
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public static void info(String message, LoggerSeverity severity)
    {
        String colour = ANSI_RESET;

        switch(severity)
        {
            case LoggerSeverity.WARNING -> colour = ANSI_YELLOW;
            case LoggerSeverity.CRITICAL -> colour = ANSI_RED;
            case LoggerSeverity.DEBUG -> colour = ANSI_GREEN;
        }

        System.out.println(colour + "[" + severity.toString() + "] " + message);
    }

    public static void info(String message)
    {
        info(message, LoggerSeverity.DEFAULT);
    }
}

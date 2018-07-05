package it.polimi.se2018.utils;

import org.fusesource.jansi.Ansi;

import java.awt.*;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Contains useful methods for logging
 */
public class Log {
    /**
     * Logs an information
     *
     * @param message the message to log
     */
    public static void i(String message) {
        System.out.println(ansi().fg(Ansi.Color.GREEN).a("[INFO] " + message).reset());
    }

    /**
     * Logs a warning
     *
     * @param message the message to log
     */
    public static void w(String message) {
        System.out.println(ansi().fg(Ansi.Color.YELLOW).a("[WARNING] " + message).reset());
    }

    /**
     * Logs a debug message
     *
     * @param message the message to log
     */
    public static void d(String message) {
        System.out.println("[DEBUG] " + message);
    }

    /**
     * Logs an error message
     *
     * @param message the message to log
     */
    public static void e(String message) {
        System.out.println(ansi().fg(Ansi.Color.RED).a("[ERROR] " + message).reset());
    }
}

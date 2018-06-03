package it.polimi.se2018.utils;

/**
 * Contains useful methods for logging
 */
public class Log {
    /**
     * Logs an information
     * @param message the message to log
     */
    public static void i(String message){
        System.out.println("[INFO] "+message);
    }

    /**
     * Logs a warning
     * @param message
     */
    public static void w(String message){
        System.out.println("[WARNING] "+message);
    }

    /**
     * Logs a debug message
     * @param message
     */
    public static void d(String message){
        System.out.println("[DEBUG] "+message);
    }

    /**
     * Logs an error message
     * @param message
     */
    public static void e(String message){
        System.out.println("[ERROR] "+message);
    }
}

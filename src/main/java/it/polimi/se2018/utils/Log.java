package it.polimi.se2018.utils;

public class Log {
    public static void i(String message){
        System.out.println("[INFO] "+message);
    }

    public static void w(String message){
        System.out.println("[WARNING] "+message);
    }

    public static void d(String message){
        System.out.println("[DEBUG] "+message);
    }

    public static void e(String message){
        System.out.println("[ERROR] "+message);
    }
}

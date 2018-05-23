package it.polimi.se2018;

import it.polimi.se2018.network.Client;
import it.polimi.se2018.network.Server;
import it.polimi.se2018.utils.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 **/

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            Log.e("You have to pass exactly one argument");
            printUsage();
        }

        switch (args[0]) {
            case "server":
                Server.startServer();
                break;
            case "client":
                Client.startClient();
                break;
            default:
                printUsage();
                break;
        }
    }

    private static void printUsage() {
        System.out.println("Usage: sagrada.jar [server|client]");
    }
}

package it.polimi.se2018;

import it.polimi.se2018.network.Client;
import it.polimi.se2018.network.Server;
import it.polimi.se2018.utils.Log;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 **/

public class App {
    public static void main(String[] args) throws RemoteException {
        if (args.length <= 0) {
            Log.e("You have to pass at least one parameter");
            printUsage();
            return;
        }

        switch (args[0]) {
            case "server":
                if(args.length<=1){
                    Log.e("Missing parameter: players");
                    printUsage();
                    return;
                }
                try{
                    new Server(Integer.parseInt(args[1])).startServer();
                }catch (NumberFormatException e){
                    Log.e("Players not a number!");
                    printUsage();
                    return;
                }

                break;
            case "client":
                Client.startClient();
                break;
            default:
                printUsage();
 /*I'M ONE STEP CLOSER TO THE EDGE, AND I'M ABOUT TO */
                break;
        }
    }

    private static void printUsage() {
        System.out.println("Usage: sagrada.jar [server players|client]");
    }
}

package it.polimi.se2018;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.network.Client;
import it.polimi.se2018.network.Server;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIPrinter;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Scanner;

/**
 * The main entry point
 **/

public class App {
    public static void main(String[] args) throws RemoteException {

        CLIPrinter.setup();

        if (args.length <= 0) {
            Log.e("You have to pass at least one parameter");
            printUsage();
            return;
        }

        switch (args[0]) {
            case "server":
                if (args.length <= 1) {
                    Log.e("Missing parameter: players");
                    printUsage();
                    return;
                }
                try {
                    Log.logLevel = true;
                    loadConfigAndStartServer(args);
                } catch (NumberFormatException e) {
                    Log.e("Players not a number!");
                    printUsage();
                    return;
                } catch (RemoteException e) {
                    Log.w("Couldn't start server.");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "client":
                Log.logLevel = false;
                Client.startClient();
                break;
            default:
                printUsage();
                /*I'M ONE STEP CLOSER TO THE EDGE, AND I'M ABOUT TO */
                break;
        }
    }

    private static void loadConfigAndStartServer(String[] args) throws IOException {

        int numPlayers = Integer.parseInt(args[1]);
        String path = null;

        if (args.length == 4) {
            // It means that also the external config file is passed as an argument... maybe.
            if (args[2].equals("--config") || args[2].startsWith("-c ")) {
                path = args[3];
            }
        }

        //File configFile = null;

        InputStream configFile = null;

        if (path == null) {
            try {
                configFile = new App().getClass().getClassLoader().getResourceAsStream("defaultConfig.json");
            } catch (Exception e) {
                e.printStackTrace();
                CLIPrinter.printError("Could not load the default configuration file!");
            }
        } else {
            configFile = new FileInputStream(new File(path));
        }

        //FileInputStream fileInputStream = new FileInputStream(configFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(configFile);
        StringBuilder builder;
        try (Scanner scanner = new Scanner(bufferedInputStream)) {
            builder = new StringBuilder();

            //read all lines
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
        }

        //now we have the whole file loaded, let's parse the JSON
        JSONObject root = new JSONObject(builder.toString());

        Log.d(root.toString());

        // Setting schema card path
        Settings.setSchemaCardDatabase(root.getString("customSchemaCardPath"));

        new Server(
            root.getInt("serverTimeout"),
            root.getInt("actionTimeout"),
            root.getString("customSchemaCardPath")
        ).startServer();

    }

    private static void printUsage() {
        System.out.println("Usage: sagrada.jar [server players|client]");
    }
}

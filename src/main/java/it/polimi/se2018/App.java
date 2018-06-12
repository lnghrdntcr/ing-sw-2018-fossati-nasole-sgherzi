package it.polimi.se2018;

import it.polimi.se2018.network.Client;
import it.polimi.se2018.network.Server;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIPrinter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
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
                if(args.length<=1){
                    Log.e("Missing parameter: players");
                    printUsage();
                    return;
                }
                try{
                    loadConfigAndStartServer(args);
                }catch (NumberFormatException e){
                    Log.e("Players not a number!");
                    printUsage();
                    return;
                } catch (RemoteException e){
                    Log.w("Couldn't start server.");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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

    private static void loadConfigAndStartServer(String[] args) throws IOException {

        int numPlayers = Integer.parseInt(args[1]);
        String path = null;

        if(args.length == 4){
            // It means that also the external config file is passed as an argument... maybe.
            if(args[2].equals("--config") || args[2].startsWith("-c ")){
                path = args[3];
            }
        }

        path = (path == null ? "gameData/resources/defaultConfig.json" : path);

        File configFile= new File(path);

        FileInputStream fileInputStream = new FileInputStream(configFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
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
            numPlayers,
            root.getInt("serverTimeout"),
            root.getInt("actionTimeout"),
            root.getString("customSchemaCardPath")
        ).startServer();

    }

    private static void printUsage() {
        System.out.println("Usage: sagrada.jar [server players|client]");
    }
}

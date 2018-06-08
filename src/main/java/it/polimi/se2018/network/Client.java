package it.polimi.se2018.network;

import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.CLI.CLI;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * The client class.
 *
 * @since 23/05/2018
 */
public class Client {

    private static String host, name, graphics;
    private static int port;
    private CLI cli;

    /**
     * Begins the connection with the RMI server.
     */
    private static void connectRMI() {

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            ServerInterface server = (ServerInterface) registry.lookup("SagradaServer");

            RemoteProxyRMI remoteRMI = new RemoteProxyRMI();
            RemoteProxyRMIInterface remoteProxyRMIInterface = (RemoteProxyRMIInterface) UnicastRemoteObject.exportObject(remoteRMI, 0);

            if (server.connectRMIClient(remoteProxyRMIInterface, name)) {
                Log.d("Connected!");
                Log.i("Hello " + name + "! Nice to meet you!");
            } else {
                Log.e("Error during connection... Duplicate name or the maximum number of players is already reached.");
            }
        } catch (NotBoundException | RemoteException e) {
            Log.e("Cannot connect to server: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Begins the connection with the RMI server.
     */
    private static void connectSocket() {
        try {
            Log.d("Connecting...");

            Socket socket = new Socket(host, port);
            Log.d("Sending name...");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(name);
            objectOutputStream.flush();
            //objectOutputStream.close();
            Log.d("Registering proxy...");
            RemoteProxySocket remoteProxySocket = new RemoteProxySocket(socket);


            Log.d("Connected!");
        } catch (IllegalArgumentException | IOException e) {
            Log.e("Cannot connect to server: " + e.getMessage());
        }

    }

    public Client() {
        super();
    }

    /**
     * Starts the client getting basic informations about the connection.
     */
    public static void startClient() {


        Scanner scanner = new Scanner(System.in);
        System.out.print("Connection method?");
        String method = scanner.nextLine();

        System.out.print("ip: ");
        host = scanner.nextLine();
        System.out.print("port: ");
        port = Integer.parseInt(scanner.nextLine());
        System.out.print("Name: ");
        name = scanner.nextLine();
        System.out.println("GUI or CLI?");
        graphics = scanner.nextLine();

        if (method.equalsIgnoreCase("RMI")) {
            connectRMI();
        } else if (method.equalsIgnoreCase("socket")) {
            connectSocket();
        } else {
            Log.w("Not implemented.");
        }
        if(graphics.equalsIgnoreCase("gui")){
            //TODO start gui
        } else {
            new CLI(name);
        }


    }

}

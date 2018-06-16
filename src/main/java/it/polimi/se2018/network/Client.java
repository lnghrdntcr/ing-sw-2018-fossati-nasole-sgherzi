package it.polimi.se2018.network;

import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.RemoteView;

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

    /**
     * Begins the connection with the RMI server.
     */
    private static RemoteProxy connectRMI() {

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
            return remoteRMI;
        } catch (NotBoundException | RemoteException e) {
            Log.e("Cannot connect to server: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Begins the connection with the RMI server.
     */
    private static RemoteProxy connectSocket() {
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
            return remoteProxySocket;
        } catch (IllegalArgumentException | IOException e) {
            Log.e("Cannot connect to server: " + e.getMessage());
        }
        return null;
    }

    public Client() {
        super();
    }

    /**
     * Starts the client getting basic informations about the connection.
     */
    public static void startClient() {

        CLIPrinter.setup();

        Scanner scanner = new Scanner(System.in);
        CLIPrinter.printQuestion("Connection method? [rmi]");
        String method = scanner.nextLine();
        method = method.equals("") ? "rmi" : method;

        int defaultPort = method.equalsIgnoreCase("rmi") ? 1099 : 2099;

        CLIPrinter.printQuestion("ip [localhost]: ");
        host = scanner.nextLine();
        host = host.equals("") ? "localhost" : host;

        CLIPrinter.printQuestion("port" + "[" + defaultPort + "]: ");
        try {
            port = Integer.parseInt(scanner.nextLine());
        } catch (RuntimeException e){
            port = defaultPort;
        }

        CLIPrinter.printQuestion("Name: ");
        name = scanner.nextLine();

        System.out.println("GUI or CLI? [CLI]");
        graphics = scanner.nextLine();
        graphics = graphics.equals("") ? "CLI" : graphics;

        RemoteProxy remoteProxy=null;
        if (method.equalsIgnoreCase("RMI")) {
            remoteProxy=connectRMI();
        } else if (method.equalsIgnoreCase("socket")) {
            remoteProxy=connectSocket();
        } else {
            Log.w("Not implemented.");
        }

        if(remoteProxy==null){
            Log.e("Connection error!");
            return;
        }


        if(graphics.equalsIgnoreCase("gui")){
            //TODO start gui
        } else {
            RemoteView remoteView = new RemoteView(name, RemoteView.Graphics.CLI);
            remoteProxy.register(remoteView);
            remoteView.register(remoteProxy);
            remoteView.start();
        }


    }

}

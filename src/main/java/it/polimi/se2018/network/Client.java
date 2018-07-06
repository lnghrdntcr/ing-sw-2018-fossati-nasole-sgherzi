package it.polimi.se2018.network;

import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.RemoteView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
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
                System.exit(-1);
            }
            return remoteRMI;
        } catch (NotBoundException | RemoteException e) {
            Log.e("Cannot connect to server: " + e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    /**
     * Begins the connection with the Socket server.
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
            System.exit(-1);
        }
        return null;
    }

    /**
     * Starts the client getting basic informations about the connection.
     */
    public static void main(String param[]) {

        CLIPrinter.setup();

        Scanner scanner = new Scanner(System.in);
        CLIPrinter.printQuestion("Connection method? [rmi], socket, json");
        String method = scanner.nextLine();
        method = (method.equals("") || (!method.equalsIgnoreCase("rmi") && !method.equalsIgnoreCase("socket"  ) && !method.equalsIgnoreCase("json"))) ? "rmi" : method;

        int defaultPort = method.equalsIgnoreCase("rmi") || (!method.equalsIgnoreCase("rmi") && !method.equalsIgnoreCase("socket")) ? 1099 : 2099;

        if(method.equalsIgnoreCase("json")){
            defaultPort=2100;
        }

        CLIPrinter.printQuestion("ip [localhost]: ");
        host = scanner.nextLine();
        host = host.equals("") ? "localhost" : host;

        CLIPrinter.printQuestion("port" + "[" + defaultPort + "]: ");
        try {
            port = Integer.parseInt(scanner.nextLine());
        } catch (RuntimeException e) {
            port = defaultPort;
        }

        CLIPrinter.printQuestion("Name: ");
        while ((name = scanner.nextLine()).equals("")) {
            CLIPrinter.printError("Invalid name");
        }

        CLIPrinter.printQuestion("GUI or CLI? [CLI]");
        graphics = scanner.nextLine();
        graphics = (graphics.equals("") || (!graphics.equalsIgnoreCase("cli") && !graphics.equalsIgnoreCase("gui"))) ? "CLI" : graphics;

        RemoteProxy remoteProxy = null;
        if (method.equalsIgnoreCase("RMI")) {
            remoteProxy = connectRMI();
        } else if (method.equalsIgnoreCase("socket")) {
            remoteProxy = connectSocket();
        } else if(method.equalsIgnoreCase("json")){
            remoteProxy =  connectJSON();
        } else {
            Log.w("Connection method " + method + " not implemented.");
        }

        if (remoteProxy == null) {
            Log.e("Connection error!");
            return;
        }

        RemoteView remoteView;
        if (graphics.equalsIgnoreCase("gui")) {
            remoteView=new RemoteView(name, RemoteView.Graphics.GUI);
        } else {
            remoteView=new RemoteView(name, RemoteView.Graphics.CLI);
        }

        remoteProxy.register(remoteView);
        remoteView.register(remoteProxy);
        remoteView.start();

    }

    /**
     * Begins a connection based on SocketString protocol (JSON based)
     */
    private static RemoteProxy connectJSON() {
        try {
            Log.d("Connecting via json..."+port);

            Socket socket = new Socket(host, port);
            Log.d("Sending name...");
            OutputStreamWriter outStream = new OutputStreamWriter(socket.getOutputStream());
            outStream.write(name+Settings.SOCKET_EOM);
            outStream.flush();
            //objectOutputStream.close();
            Log.d("Registering proxy...");
            RemoteProxySocketString remoteProxySocket = new RemoteProxySocketString(socket);


            Log.d("Connected!");
            return remoteProxySocket;
        } catch (IllegalArgumentException | IOException e) {
            Log.e("Cannot connect to server: " + e.getMessage());
            System.exit(-1);
        }
        return null;
    }

}

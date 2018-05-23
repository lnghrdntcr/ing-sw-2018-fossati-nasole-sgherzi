package it.polimi.se2018.network;

import it.polimi.se2018.utils.Log;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * The client class.
 * @author Francesco Sgherzi
 * @since 23/05/2018
 */
public class Client {

    private static String host;
    private static int port;

    /**
     * Begins the connection with the RMI server.
     */
    private static void connectRMI(){

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            ServerInterface server = (ServerInterface) registry.lookup( "SagradaServer");
            Log.d("Looked up");
            RemoteProxyRMI remoteRMI = new RemoteProxyRMI();
            RemoteProxyRMIInterface remoteProxyRMIInterface = (RemoteProxyRMIInterface) UnicastRemoteObject.exportObject(remoteRMI, 0);
            Log.d("Exported");
            server.connectRMIClient(remoteProxyRMIInterface);
            Log.d("Connected");
        } catch (NotBoundException | RemoteException e) {
            Log.e("Cannot connect to RMI server, catching those madaffada: ");
            e.printStackTrace();
        }

    }

    /**
     * Begins the connection with the RMI server.
     */
    private static void connectSocket() {

    }

    /**
     * Starts the client getting basic informations about the connection.
     */
    public static void startClient(){


        Scanner scanner = new Scanner(System.in);
        System.out.println("Connection method?");
        String method = scanner.nextLine();

        if(method.equalsIgnoreCase("RMI")){

            System.out.println("ip: ");
            host = scanner.nextLine();
            System.out.println("port: ");
            port = scanner.nextInt();

            connectRMI();
        } else {
            Log.w("Not implemented.");
            return;
        }


    }

}

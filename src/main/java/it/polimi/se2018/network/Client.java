package it.polimi.se2018.network;

import it.polimi.se2018.utils.Log;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client {

    private static String host;
    private static int port;

    private static void connectRMI(){

        try {

            ServerInterface server = (ServerInterface) Naming.lookup("//" + host + ":" + port + "/SagradaServer");
            RemoteProxyRMI remoteRMI = new RemoteProxyRMI();
            RemoteProxyRMIInterface remoteProxyRMIInterface = (RemoteProxyRMIInterface) UnicastRemoteObject.exportObject(remoteRMI, 0);
            server.connectRMIClient(remoteProxyRMIInterface);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            Log.e("Cannot connect to RMI server, catching those madaffada: " + e.getMessage());
        }

    }

    private static void connectSocket() {

    }

    public static void startClient(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("ip: ");
        host = scanner.nextLine();
        System.out.println("port: ");
        port = scanner.nextInt();

        connectRMI();
    }

}

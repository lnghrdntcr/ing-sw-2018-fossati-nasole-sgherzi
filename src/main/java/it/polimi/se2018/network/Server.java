package it.polimi.se2018.network;

import it.polimi.se2018.utils.Log;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInterface  {
    public final static int PORT=1099;

    protected Server() throws RemoteException {
    }

    public static void startServer(){
        setupRMI();
    }

    private static void setupRMI(){
        try {
            LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            //TODO
            return;
        }

        try {
            Server serverImplementation = new Server();
            Naming.rebind("//localhost/SagradaServer", serverImplementation);
            Log.i("RMI Server exposed up on port "+ PORT);
        } catch (MalformedURLException e) {
            System.err.println("Impossibile registrare l'oggetto indicato!");
        } catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        }
    }


    @Override
    public void connectRMIClient(RemoteProxyRMI remoteProxyRMI) throws RemoteException {

    }
}

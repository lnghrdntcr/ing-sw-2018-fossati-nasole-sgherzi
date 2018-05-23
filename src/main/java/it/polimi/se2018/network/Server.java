package it.polimi.se2018.network;

import it.polimi.se2018.utils.Log;

import java.net.*;
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

        String ip = "127.0.0.1";

        try (final DatagramSocket socket = new DatagramSocket()){

            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();

            System.setProperty("java.rmi.server.hostname", ip);
            LocateRegistry.createRegistry(PORT);

        } catch (RemoteException e) {
            //TODO
            System.out.println(e.getMessage());
            return;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            Server serverImplementation = new Server();
            Naming.rebind("SagradaServer", serverImplementation);
            Log.i("RMI Server exposed up on " + ip + ":" + PORT);

        } catch (MalformedURLException e) {
            System.err.println("Impossibile registrare l'oggetto indicato!");
        } catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        }
    }


    @Override
    public void connectRMIClient(RemoteProxyRMIInterface remoteProxyRMI) throws RemoteException {
        Log.e("Ti piacerebbe eh....");
    }
}

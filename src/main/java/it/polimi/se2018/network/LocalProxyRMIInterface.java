package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An interface to expose the server's methods to the client via RMI connection
 */
public interface LocalProxyRMIInterface extends Remote {
    /**
     * @param event
     * @throws RemoteException if an error occours
     */
    public void sendEventToServer(Event event) throws RemoteException;
}

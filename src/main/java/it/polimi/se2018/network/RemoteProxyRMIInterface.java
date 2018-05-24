package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An interface to expose client's methods to the server via RMI connection
 */
public interface RemoteProxyRMIInterface extends Remote {
    /**
     * Sends the specified event to the client
     * @param event the event to dispatch to the {@link it.polimi.se2018.view.RemoteView}
     * @throws RemoteException if an error occours in the connection
     */
    public void sendEventToClient(Event event) throws RemoteException;

    /**
     * Allows the server to set himself, so it can be called by the client
     * @param localProxyRMI the rapresentation of the server
     * @throws RemoteException if an error occours in the connection
     */
    public void setServer(LocalProxyRMIInterface localProxyRMI) throws RemoteException;
}

package it.polimi.se2018.network;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * An interface to expose Server's methods to allow RMI clients to connect
 */
public interface ServerInterface extends Remote {
    /**
     * A method that must be called from a remote client via RMI.
     * Tries to connect the client with this server
     * @param remoteProxyRMI the server's representation on client side
     * @param player the nickname of the player
     * @return true if the connection is successful, false otherwise
     * @throws RemoteException if there is a communication error
     */
    public boolean connectRMIClient(RemoteProxyRMIInterface remoteProxyRMI, String player) throws RemoteException;
}

package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;

import java.rmi.RemoteException;

/**
 * The LocalProxy, retained by the server.
 * @author Francesco Sgherzi
 * @since 23/05/2018
 */
public class LocalProxyRMI extends LocalProxy implements LocalProxyRMIInterface{

    private RemoteProxyRMIInterface client;

    /**
     * Sends the modelEvent from the LocalProxy to the RemoteProxy using RMI.
     * @param event The modelEvent to be sent to the client.
     */
    @Override
    public void sendEventToClient(Event event) {
        try {
            client.sendEventToClient(event);
        } catch (RemoteException e) {
            Log.e("Failed to send modelEvent to client." + e.getMessage());
        }
    }

    /**
     * Sends modelEvent to the VirtualView, in order to be dispatched by it.
     * @param event The modelEvent to be sent.
     * @throws RemoteException If an error occurred.
     */
    @Override
    public void sendEventToServer(Event event) throws RemoteException {
        this.dispatchEventToVirtualView(event);
    }
}
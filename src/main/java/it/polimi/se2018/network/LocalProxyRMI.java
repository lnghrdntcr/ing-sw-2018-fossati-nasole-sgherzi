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
     * Sends the event from the LocalProxy to the RemoteProxy using RMI.
     * @param event The event to be sent to the client.
     */
    @Override
    public void sendEventToClient(Event event) {
        try {
            client.sendEventToClient(event);
        } catch (RemoteException e) {
            Log.e("Failed to send event to client." + e.getMessage());
        }
    }

    /**
     * Sends event to the VirtualView, in order to be dispatched by it.
     * @param event The event to be sent.
     * @throws RemoteException If an error occurred.
     */
    @Override
    public void sendEventToServer(Event event) throws RemoteException {
        this.dispatchEventToVirtualView(event);
    }
}

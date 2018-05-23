package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;

import java.rmi.RemoteException;

/**
 * The LocalProxy, retained by the client.
 * @author Francesco Sgherzi
 * @since 23/05/2018
 */
public class RemoteProxyRMI extends RemoteProxy implements RemoteProxyRMIInterface {

    private LocalProxyRMIInterface localProxyRMI;

    /**
     * Sends the event to the LocalProxy, using RMI.
     * @param event The event to be sent.
     */
    @Override
    void sendEventToServer(Event event) {
        try {
            localProxyRMI.sendEventToServer(event);
        } catch (RemoteException e) {
            Log.e("Failed to send event to server: " + e.getMessage());
        }
    }

    /**
     * Sends event to the RemoteView, in order to be dispatched by it.
     * @param event The event to be sent.
     * @throws RemoteException If an error occurred.
     */
    @Override
    public void sendEventToClient(Event event) throws RemoteException {
        // This method is called from server side when an event occurs on the server.
        this.dispatchEventToRemoteView(event);
    }

    public LocalProxyRMIInterface getLocalProxyRMI() {
        return localProxyRMI;
    }

    public void setLocalProxyRMI(LocalProxyRMIInterface localProxyRMI) {
        this.localProxyRMI = localProxyRMI;
    }
}

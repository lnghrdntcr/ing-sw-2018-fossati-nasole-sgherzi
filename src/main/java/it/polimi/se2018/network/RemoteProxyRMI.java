package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.rmi.RemoteException;

/**
 * The LocalProxy, retained by the client.
 * @since 23/05/2018
 */
public class RemoteProxyRMI extends RemoteProxy implements RemoteProxyRMIInterface {

    private LocalProxyRMIInterface localProxyRMI;

    /**
     * Sends the modelEvent to the LocalProxy, using RMI.
     * @param event The modelEvent to be sent.
     */
    @Override
    void sendEventToServer(ViewEvent event) {
        try {
            localProxyRMI.sendEventToServer( event);
        } catch (RemoteException e) {
            Log.e("Failed to send modelEvent to server: " + e.getMessage());
        }
    }

    /**
     * Sends modelEvent to the RemoteView, in order to be dispatched by it.
     * @param event The modelEvent to be sent.
     * @throws RemoteException If an error occurred.
     */
    @Override
    public void sendEventToClient(Event event) throws RemoteException {
        // This method is called from server side when an modelEvent occurs on the server.
        this.dispatchEventToRemoteView(event);
    }

    public LocalProxyRMIInterface getServer() {
        return localProxyRMI;
    }

    public void setServer(LocalProxyRMIInterface localProxyRMI) throws RemoteException {
        this.localProxyRMI = localProxyRMI;
    }
}

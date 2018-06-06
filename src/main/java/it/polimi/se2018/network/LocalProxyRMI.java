package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.PlayerDisconnectedEvent;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.rmi.RemoteException;

/**
 * The LocalProxy, retained by the server.
 *
 * @since 23/05/2018
 */
public class LocalProxyRMI extends LocalProxy implements LocalProxyRMIInterface {

    private RemoteProxyRMIInterface client;

    /**
     * Sends the modelEvent from the LocalProxy to the RemoteProxy using RMI.
     *
     * @param event The modelEvent to be sent to the client.
     */
    @Override
    public void sendEventToClient(Event event) {
        try {
            client.sendEventToClient(event);
        } catch (RemoteException e) {
            // If it catches an exception it means that the client is disconnected.
            // TODO: send the PlayerDisconnectedEvent
            this.getView().disconnect();
            Log.e("Unable to send an event to the client, client disconnected!");
        }
    }

    /**
     * Sends modelEvent to the VirtualView, in order to be dispatched by it.
     *
     * @param event The modelEvent to be sent.
     * @throws RemoteException If an error occurred.
     */
    @Override
    public void sendEventToServer(ViewEvent event) throws RemoteException {
        this.dispatchEventToVirtualView(event);
    }

    public RemoteProxyRMIInterface getClient() {
        return client;
    }

    public void setClient(RemoteProxyRMIInterface client) {
        this.client = client;
    }
}

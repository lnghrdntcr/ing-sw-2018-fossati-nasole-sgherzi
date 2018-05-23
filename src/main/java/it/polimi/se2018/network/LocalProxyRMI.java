package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;

import java.rmi.RemoteException;

public class LocalProxyRMI extends LocalProxy implements LocalProxyRMIInterface{

    private RemoteProxyRMIInterface client;

    @Override
    public void sendEventToClient(Event event) {
        try {
            client.sendEventToClient(event);
        } catch (RemoteException e) {
            Log.e("Failed to send event to client." + e.getMessage());
        }
    }

    @Override
    public void sendEventToServer(Event event) throws RemoteException {
        this.dispatchEventToVirtualView(event);
    }
}

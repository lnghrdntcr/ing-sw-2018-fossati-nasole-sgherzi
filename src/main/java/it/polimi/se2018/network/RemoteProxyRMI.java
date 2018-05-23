package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;

import java.rmi.RemoteException;

public class RemoteProxyRMI extends RemoteProxy implements RemoteProxyRMIInterface {

    private LocalProxyRMIInterface localProxyRMI;

    @Override
    void sendEventToServer(Event event) {
        try {
            localProxyRMI.sendEventToServer(event);
        } catch (RemoteException e) {
            Log.e("Failed to send event to server: " + e.getMessage());
        }
    }

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

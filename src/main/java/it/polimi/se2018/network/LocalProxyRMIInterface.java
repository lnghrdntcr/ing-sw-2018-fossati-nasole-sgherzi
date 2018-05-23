package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LocalProxyRMIInterface extends Remote {
    public void sendEventToServer(Event event) throws RemoteException;
}

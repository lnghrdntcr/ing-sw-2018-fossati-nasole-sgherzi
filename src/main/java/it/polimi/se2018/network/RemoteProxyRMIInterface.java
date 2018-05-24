package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteProxyRMIInterface extends Remote {
    public void sendEventToClient(Event event) throws RemoteException;
    public void setServer(LocalProxyRMIInterface localProxyRMI) throws RemoteException;
}

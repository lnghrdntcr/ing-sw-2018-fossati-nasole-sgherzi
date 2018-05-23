package it.polimi.se2018.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void connectRMIClient(RemoteProxyRMI remoteProxyRMI) throws RemoteException;
}

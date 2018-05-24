package it.polimi.se2018.network;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.VirtualView;

import javax.xml.bind.Element;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements ServerInterface {
    private ArrayList<VirtualView> virtualViews = new ArrayList<>();
    private Controller controller;
    private int playersN;
    private ServerSocket serverSocket;
    private Thread listenerThread;
    boolean gameStarted;

    private final static int RMI_PORT = 1099;
    private final static int SOCKET_PORT = 2099;
    private String ip = "127.0.0.1";

    public Server(int playersN) throws RemoteException {
        this.playersN = playersN;
    }

    public void startServer() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            Log.w("Cannot auto determine the ip address for the current server. Falling back to default.");
        }

        setupRMI();
        setupSocket();
    }

    private void setupSocket() {
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            Log.i("Socket server listening on " + ip + ":" + SOCKET_PORT);
            listenerThread = new Thread(() -> {
                try {
                    while (true) {
                        Socket clientConnection = serverSocket.accept();
                        ObjectInputStream inputStream = new ObjectInputStream(clientConnection.getInputStream());
                        String playerName = null;
                        try {
                            playerName = (String) inputStream.readObject();
                        } catch (ClassNotFoundException | ClassCastException e) {
                            Log.w("Cannot determine player's name. Disconnecting...");
                            clientConnection.close();
                        }

                        if (playerName != null) {
                            Log.i("Hello "+playerName+"! Nice to meet you!");
                            for (VirtualView el : virtualViews) {
                                if (el.getPlayer().equals(playerName)) {
                                    Log.w("A player with an already existent name tried to join this match!");
                                    clientConnection.close();
                                }
                            }

                            if (!clientConnection.isClosed()) {
                                //inputStream.close();
                                LocalProxySocket localProxySocket = new LocalProxySocket(clientConnection);
                                addClient(localProxySocket, playerName);
                                Log.d("Connected!");
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupRMI() {
        try {

            System.setProperty("java.rmi.server.hostname", ip);
            LocateRegistry.createRegistry(RMI_PORT);
        } catch (RemoteException e) {
            Log.e("Cannot create RMI server: " + e.getMessage());
        }

        try {
            Naming.rebind("SagradaServer", this);
            Log.i("RMI Server exposed up on " + ip + ":" + RMI_PORT);
        } catch (MalformedURLException | RemoteException e) {
            Log.e("Cannot register RMI server!");
        }
    }


    @Override
    public boolean connectRMIClient(RemoteProxyRMIInterface remoteProxyRMI, String player) throws RemoteException {
        for (VirtualView el : virtualViews) {
            if (el.getPlayer().equals(player)) {
                Log.w("A player with an already existent name tried to join this match!");
                return false;
            }
        }

        LocalProxyRMI localProxyRMI = new LocalProxyRMI();
        LocalProxyRMIInterface localProxyRMIInterface = (LocalProxyRMIInterface) UnicastRemoteObject.exportObject(localProxyRMI, 0);
        localProxyRMI.setClient(remoteProxyRMI);

        remoteProxyRMI.setServer(localProxyRMIInterface);

        Log.i("Client connected!");
        addClient(localProxyRMI, player);
        return true;
    }

    private void addClient(LocalProxy localProxy, String player) {
        VirtualView virtualView = new VirtualView(player, localProxy);
        localProxy.setView(virtualView);

        virtualViews.add(virtualView);

        if (virtualViews.size() == playersN) {
            startGame();
        }
    }

    private void startGame() {
        //TODO
        gameStarted = true;
        //listenerThread.stop();

    }


}


















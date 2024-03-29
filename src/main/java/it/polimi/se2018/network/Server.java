package it.polimi.se2018.network;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.VirtualView;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The entry point for the server. Manages all the connections and waits for client's connections
 */
public class Server extends UnicastRemoteObject implements ServerInterface {


    private final static int RMI_PORT = 1099;
    private final static int SOCKET_PORT = 2099;
    private static final int SOCKETSTRING_PORT = 2100;
    private final long actionTimeout;
    private final String customSchemaCardPath;
    boolean gameStarted;
    private ArrayList<View> virtualViews = new ArrayList<>();
    private ServerSocket serverSocket;
    private Thread listenerThread;
    private Thread timeoutThread;
    // Config variables.
    private long serverTimeout;
    // The beginning time of the timeout.
    private long beginTime;
    private String ip = "127.0.0.1";
    private Controller controller;
    private ServerSocket serverSocketString;
    private Thread listenerThreadString;

    /**
     * Instantiate a new server
     *
     * @throws RemoteException If a communicaton error occours
     */
    public Server(int serverTimeout, int actionTimeout, String customSchemaCardPath) throws RemoteException {
        this.serverTimeout = serverTimeout * 1000L;
        this.actionTimeout = actionTimeout * 1000L;
        this.customSchemaCardPath = customSchemaCardPath;
    }

    /**
     * Main entry point. Starts the RMI and the Socket listeners
     */
    public void startServer() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            Log.w("Cannot auto determine the ip address for the current server. Falling back to default.");
        }

        setupRMI();
        setupSocket();
        setupSocketString();
    }

    /**
     * Instantiates the Socket part of the servers.
     * Moreover spawns a Thread to accept incoming connections
     */
    private void setupSocket() {
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            Log.i("Socket server listening on " + ip + ":" + SOCKET_PORT);
            listenerThread = new Thread(() -> {

                try {
                    while (virtualViews.size() < 4) {
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
                            Log.i("Hello " + playerName + "! Nice to meet you!");

                            if (this.controller != null && this.controller.isPlayerDisconnected(playerName)) {
                                this.controller.reconnectPlayer(new LocalProxySocket(clientConnection), playerName);
                            } else {

                                for (View el : virtualViews) {
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

                    }

                    Log.w(
                        "No more player are allowed as the maximum of 4 players is already reached."
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instantiates the SocketString (JSON) part of the servers.
     * Moreover spawns a Thread to accept incoming connections
     */

    private void setupSocketString() {
        try {
            serverSocketString = new ServerSocket(SOCKETSTRING_PORT);
            Log.i("SocketString server listening on " + ip + ":" + SOCKETSTRING_PORT);
            listenerThreadString = new Thread(() -> {

                try {
                    while (virtualViews.size() < 4) {
                        Socket clientConnection = serverSocketString.accept();
                        InputStreamReader inputStream = new InputStreamReader(clientConnection.getInputStream());
                        String playerName = null;

                        StringBuilder builder = new StringBuilder();
                        while (builder.indexOf(Settings.SOCKET_EOM) == -1) {
                            char[] buffer = new char[256];
                            int readed = inputStream.read(buffer, 0, 256);
                            builder.append(buffer, 0, readed);
                        }
                        playerName = builder.substring(0, builder.indexOf(Settings.SOCKET_EOM));


                        if (playerName != null) {
                            Log.i("Hello " + playerName + "! Nice to meet you!");

                            if (this.controller != null && this.controller.isPlayerDisconnected(playerName)) {
                                this.controller.reconnectPlayer(new LocalProxySocketString(clientConnection), playerName);
                            } else {

                                for (View el : virtualViews) {
                                    if (el.getPlayer().equals(playerName)) {
                                        Log.w("A player with an already existent name tried to join this match!");
                                        clientConnection.close();
                                    }
                                }

                                if (!clientConnection.isClosed()) {
                                    //inputStream.close();
                                    LocalProxySocketString localProxySocket = new LocalProxySocketString(clientConnection);
                                    addClient(localProxySocket, playerName);
                                    Log.d("Connected!");
                                }
                            }


                        }

                    }

                    Log.w(
                        "No more player are allowed as the maximum of 4 players is already reached."
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            listenerThreadString.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the timeout when more than one user is connected
     */
    private void startTimeout() {
        this.timeoutThread = new Thread(() -> {
            Log.d("Starting timeout with timeout of " + this.serverTimeout / 1000L + " seconds");
            this.beginTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - this.beginTime) < this.serverTimeout && !gameStarted) {
                try {
                    Thread.sleep(1000);
                    Log.d("[CONNECTION] Remaining " + (this.serverTimeout / 1000L - this.getTimeout()) + " seconds");
                } catch (InterruptedException ignored) {
                }
            }

            if (virtualViews.size() < 4 && virtualViews.size() > 1 && !gameStarted) this.startGame();

        });

        this.timeoutThread.start();

    }

    private int getTimeout() {
        return (int) ((System.currentTimeMillis() - this.beginTime) / 1000L);
    }

    /**
     * Starts the RMI server and listen for RMI clients
     */
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


    /**
     * A method that must be called from a remote client via RMI.
     * Tries to connect the client with this serverCome cCome
     *
     * @param remoteProxyRMI the server's representation on client side
     * @param player         the nickname of the player
     * @return true if the connection is successful, false otherwise
     * @throws RemoteException if there is a communication error
     */
    @Override
    public boolean connectRMIClient(RemoteProxyRMIInterface remoteProxyRMI, String player) throws RemoteException {

        if (virtualViews.size() >= 4) return false;

        if (this.controller != null && this.controller.isPlayerDisconnected(player)) {

            LocalProxyRMI localProxyRMI = new LocalProxyRMI();
            LocalProxyRMIInterface localProxyRMIInterface = (LocalProxyRMIInterface) UnicastRemoteObject.exportObject(localProxyRMI, 0);
            localProxyRMI.setClient(remoteProxyRMI);

            remoteProxyRMI.setServer(localProxyRMIInterface);

            this.controller.reconnectPlayer(localProxyRMI, player);

            Log.i("Client with name " + player + " reconnected!");
            return true;
        }

        for (View el : virtualViews) {
            if (el.getPlayer().equals(player)) {
                Log.w("A player with an already existent name tried to join this match!");
                return false;
            }
        }

        LocalProxyRMI localProxyRMI = new LocalProxyRMI();
        LocalProxyRMIInterface localProxyRMIInterface = (LocalProxyRMIInterface) UnicastRemoteObject.exportObject(localProxyRMI, 0);
        localProxyRMI.setClient(remoteProxyRMI);

        remoteProxyRMI.setServer(localProxyRMIInterface);

        Log.i("Client with name " + player + " connected!");

        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addClient(localProxyRMI, player);
        }).start();

        return true;
    }

    /**
     * Creates a {@link VirtualView} and associates it with the localProxy
     *
     * @param localProxy the local proxy to add to the list of connected clients
     * @param player     player name
     */
    private void addClient(LocalProxy localProxy, String player) {
        VirtualView virtualView = new VirtualView(player, localProxy);
        localProxy.setView(virtualView);

        virtualViews.add(virtualView);

        if (virtualViews.size() == 4 && !gameStarted) {
            startGame();
            return;
        }

        if (virtualViews.size() == 2) this.startTimeout();
    }

    /**
     * When the required number of players are connected, starts the match
     */
    private void startGame() {
        gameStarted = true;

        this.controller = new Controller(virtualViews, this.actionTimeout);
        this.controller.start();

    }


    public static void main(String args[]) throws IOException {
        CLIPrinter.setup();
        String path = null;
        Log.logLevel=true;

        if (args.length == 4) {
            // It means that also the external config file is passed as an argument... maybe.
            if (args[1].equals("--config") || args[2].startsWith("-c ")) {
                path = args[2];
            }
        }


        InputStream configFile = null;

        if (path == null) {
            try {
                configFile = Server.class.getClassLoader().getResourceAsStream("defaultConfig.json");
            } catch (Exception e) {
                e.printStackTrace();
                CLIPrinter.printError("Could not load the default configuration file!");
            }
        } else {
            configFile = new FileInputStream(new File(path));
        }

        //FileInputStream fileInputStream = new FileInputStream(configFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(configFile);
        StringBuilder builder;
        try (Scanner scanner = new Scanner(bufferedInputStream)) {
            builder = new StringBuilder();

            //read all lines
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
        }

        //now we have the whole file loaded, let's parse the JSON
        JSONObject root = new JSONObject(builder.toString());

        Log.d(root.toString());

        // Setting schema card path
        Settings.setSchemaCardDatabase(root.getString("customSchemaCardPath"));

        new Server(
                root.getInt("serverTimeout"),
                root.getInt("actionTimeout"),
                root.getString("customSchemaCardPath")
        ).startServer();

    }


}


















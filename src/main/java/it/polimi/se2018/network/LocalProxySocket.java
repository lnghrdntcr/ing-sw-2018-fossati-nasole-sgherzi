package it.polimi.se2018.network;

import it.polimi.se2018.model.modelEvent.TurnChangedEvent;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.PlayerDisconnectedEvent;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A class that represent a client connected via socket communication on the server.
 */
public class LocalProxySocket extends LocalProxy {
    private Socket remoteConnection;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ListenerThread listenerThread;

    /**
     * Create a new proxy, associating the specified remoteConnection
     *
     * @param remoteConnection the socket to associate with this instance of the proxy
     * @throws IOException if an error occours
     */
    public LocalProxySocket(Socket remoteConnection) throws IOException {
        this.remoteConnection = remoteConnection;
        // In this specific order or it does not work!
        objectOutputStream = new ObjectOutputStream(remoteConnection.getOutputStream());
        objectInputStream = new ObjectInputStream(remoteConnection.getInputStream());

        listenerThread = new ListenerThread();
        listenerThread.start();

        /*while (true){
            sendEventToClient(new TurnChangedEvent("LOCAL", "LOCALPLAYER", 0));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * Sends the event to the connected client, using socket connection
     *
     * @param event the event that should me dispatched
     */
    @Override
    synchronized public void sendEventToClient(Event event) {
        try {
            objectOutputStream.writeObject(event);
            objectOutputStream.flush();
        } catch (IOException e) {
            // If it catches an exception it means that the client is disconnected.
            this.getView().disconnect();
            Log.e("Unable to send an event to the client, client disconnected!");
        }
    }

    /**
     * A thread that listen to incoming event and dispatch them to the {@link it.polimi.se2018.view.VirtualView}
     */
    class ListenerThread extends Thread {
        private boolean goAhead = true;

        @Override
        public void run() {
            Log.d("Listener thread started for " + LocalProxySocket.this.remoteConnection.getInetAddress());
            ViewEvent event;
            try {
                while (goAhead && !LocalProxySocket.this.remoteConnection.isClosed()) {
                    try {
                        event = (ViewEvent) objectInputStream.readObject();
                        Log.d("Received " + event);
                        LocalProxySocket.this.dispatchEventToVirtualView(event);
                    } catch (ClassNotFoundException | ClassCastException e) {
                        Log.e("Invalid event received: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                Log.d("ListenerThread: IOException: " + e.getMessage());
            }
            Log.d("Client disconnected!");
        }

        /**
         * Tries to stop the thread and tries to close the connection
         */
        void kill() {
            goAhead = false;
            try {
                objectInputStream.close();
            } catch (IOException e) {
                Log.e("Error during stram closing: " + e.getMessage());
            }
        }
    }

    /**
     * Kill the {@link ListenerThread} and close any active connection with the client
     */
    public void closeConnection() {
        listenerThread.kill();
        try {
            objectOutputStream.close();
            remoteConnection.close();
        } catch (IOException e) {
            Log.e("Error during stram closing: " + e.getMessage());
        }

    }
}

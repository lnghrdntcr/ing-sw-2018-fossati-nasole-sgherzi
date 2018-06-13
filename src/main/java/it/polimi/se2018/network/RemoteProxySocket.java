package it.polimi.se2018.network;

import it.polimi.se2018.model.modelEvent.TurnChangedEvent;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A class that represent a server connected via socket communication on the client.
 */
public class RemoteProxySocket extends RemoteProxy {
    private Socket remoteConnection;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private RemoteProxySocket.ListenerThread listenerThread;

    /**
     * Create a new proxy, associating the specified remoteConnection
     * @param remoteConnection the socket to associate with this instance of the proxy
     * @throws IOException if an error occours
     */
    RemoteProxySocket(Socket remoteConnection) throws IOException {
        this.remoteConnection = remoteConnection;
        // In this specific order or it does not work!
        objectOutputStream = new ObjectOutputStream(remoteConnection.getOutputStream());
        objectInputStream = new ObjectInputStream(remoteConnection.getInputStream());

        listenerThread = new RemoteProxySocket.ListenerThread();
        listenerThread.start();

        /*while (true){
            sendEventToServer(new TurnChangedEvent("REMOTE", "REMOTEPLAYER", 0));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * Sends the event to the connected server, using socket connection
     * @param event the event that should me dispatched
     */
    @Override
    void sendEventToServer(ViewEvent event) {
        try {
            objectOutputStream.writeObject(event);
        } catch (IOException e) {
            Log.e("Unable to send an event to the server!");
        }
    }

    /**
     * A thread that listen to incoming event and dispatch them to the {@link it.polimi.se2018.view.RemoteView}
     */
    class ListenerThread extends Thread {
        private boolean goAhead = true;

        @Override
        public void run() {
            Log.d("Listener thread started for " + RemoteProxySocket.this.remoteConnection.getInetAddress());
            Event event;
            try {
                while (goAhead && !RemoteProxySocket.this.remoteConnection.isClosed()) {
                    try {
                        event = (Event) objectInputStream.readObject();
                        Log.d("Received " + event);
                        RemoteProxySocket.this.dispatchEventToRemoteView(event);
                    } catch (ClassNotFoundException | ClassCastException e) {
                        Log.e("Invalid event received: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }catch (IOException e){
                Log.d("ListenerThread: IOException: "+e.getMessage());
            }

            Log.d("Disconnected!");
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

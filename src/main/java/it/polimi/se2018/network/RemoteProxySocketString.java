package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.viewEvent.ViewEvent;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;

/**
 * A class that represent a server connected via socket communication on the client.
 */
public class RemoteProxySocketString extends RemoteProxy {
    private Socket remoteConnection;
    private InputStreamReader objectInputStream;
    private OutputStreamWriter objectOutputStream;
    private RemoteProxySocketString.ListenerThread listenerThread;

    /**
     * Create a new proxy, associating the specified remoteConnection
     *
     * @param remoteConnection the socket to associate with this instance of the proxy
     * @throws IOException if an error occours
     */
    RemoteProxySocketString(Socket remoteConnection) throws IOException {
        this.remoteConnection = remoteConnection;
        // In this specific order or it does not work!
        objectOutputStream =  new OutputStreamWriter(new BufferedOutputStream(remoteConnection.getOutputStream()));
        objectInputStream = new InputStreamReader(new BufferedInputStream(remoteConnection.getInputStream()));

        listenerThread = new RemoteProxySocketString.ListenerThread();
        listenerThread.start();

    }

    /**
     * Sends the event to the connected server, using socket connection
     *
     * @param event the event that should me dispatched
     */
    @Override
    void sendEventToServer(ViewEvent event) {
        try {
            objectOutputStream.write(event.toJSON().toString());
            objectOutputStream.write(Settings.SOCKET_EOM);
            objectOutputStream.flush();
        } catch (IOException e) {
            Log.e("Unable to send an event to the server!");
            System.exit(-1);
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

    /**
     * A thread that listen to incoming event and dispatch them to the {@link it.polimi.se2018.view.RemoteView}
     */
    class ListenerThread extends Thread {
        private boolean goAhead = true;
        StringBuilder builder = new StringBuilder();

        char buffer[] = new char[256];

        @Override
        public void run() {
            Log.d("Listener thread started for " + RemoteProxySocketString.this.remoteConnection.getInetAddress());
            Event event;
            try {
                while (goAhead && !RemoteProxySocketString.this.remoteConnection.isClosed()) {
                    try {
                        int characterReaded = objectInputStream.read(buffer);
                        builder.append(buffer, 0, characterReaded);


                        while(builder.indexOf(Settings.SOCKET_EOM) != -1) {
                            event =Event.decodeJSON(builder.substring(0, builder.indexOf(Settings.SOCKET_EOM)));

                            RemoteProxySocketString.this.dispatchEventToRemoteView(event);

                            builder.delete(0, builder.indexOf(Settings.SOCKET_EOM)+1);
                        }
                    } catch (ClassNotFoundException |ClassCastException e) {
                        Log.e("Invalid event received: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                Log.d("ListenerThread: IOException: " + e.getMessage());

            }

            Log.d("Disconnected!");
            System.exit(-1);

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
}

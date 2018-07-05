package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.io.*;
import java.net.Socket;

/**
 * A class that represent a client connected via socket communication on the server.
 */
public class LocalProxySocketString extends LocalProxy {
    private Socket remoteConnection;
    private InputStreamReader objectInputStream;
    private OutputStreamWriter objectOutputStream;
    private ListenerThread listenerThread;

    /**
     * Create a new proxy, associating the specified remoteConnection
     *
     * @param remoteConnection the socket to associate with this instance of the proxy
     * @throws IOException if an error occours
     */
    public LocalProxySocketString(Socket remoteConnection) throws IOException {
        this.remoteConnection = remoteConnection;
        // In this specific order or it does not work!
        objectOutputStream =  new OutputStreamWriter(new BufferedOutputStream(remoteConnection.getOutputStream()));
        objectInputStream = new InputStreamReader(new BufferedInputStream(remoteConnection.getInputStream()));

        listenerThread = new ListenerThread();
        listenerThread.start();

    }

    /**
     * Sends the event to the connected client, using socket connection
     *
     * @param event the event that should me dispatched
     */
    @Override
    synchronized public void sendEventToClient(Event event) {
        //check if this event is for me
        if (
            event.getReceiver().equals(getView().getPlayer()) || // Message is for me.
                event.getReceiver().equals("")                       // Message is for everyone.
            ) {
            try {
                objectOutputStream.write(event.toJSON().toString());
                objectOutputStream.write(Settings.SOCKET_EOM);
                objectOutputStream.flush();
            } catch (IOException e) {
                Log.d(e.getMessage());
                // If it catches an exception it means that the client is disconnected.
                this.getView().disconnect();
                Log.e("Unable to send an event to the client, client disconnected!");
            }
        }
    }

    /**
     * A thread that listen to incoming event and dispatch them to the {@link it.polimi.se2018.view.VirtualView}
     */
    class ListenerThread extends Thread {
        private boolean goAhead = true;
        StringBuilder builder = new StringBuilder();

        char buffer[] = new char[256];

        @Override
        public void run() {
            Log.d("Listener thread started for " + LocalProxySocketString.this.remoteConnection.getInetAddress());
            ViewEvent event;
            try {
                while (goAhead && !LocalProxySocketString.this.remoteConnection.isClosed()) {
                    try {
                        int characterReaded = objectInputStream.read(buffer);
                        if(characterReaded<=0 ) continue;
                        builder.append(buffer, 0, characterReaded);


                        while(builder.indexOf(Settings.SOCKET_EOM) != -1) {
                            String message = builder.substring(0, builder.indexOf(Settings.SOCKET_EOM));
                            System.out.println("JSON: "+message);
                            event =(ViewEvent) Event.decodeJSON(message);

                            LocalProxySocketString.this.dispatchEventToVirtualView(event);

                            builder.delete(0, builder.indexOf(Settings.SOCKET_EOM)+Settings.SOCKET_EOM.length());
                        }
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

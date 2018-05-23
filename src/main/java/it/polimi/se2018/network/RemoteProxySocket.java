package it.polimi.se2018.network;

import it.polimi.se2018.model.modelEvent.TurnChangedEvent;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RemoteProxySocket extends RemoteProxy {
    private Socket remoteConnection;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private RemoteProxySocket.ListenerThread listenerThread;

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

    @Override
    void sendEventToServer(Event event) {
        try {
            objectOutputStream.writeObject(event);
        } catch (IOException e) {
            Log.e("Unbale to send an event to the client!");
        }
    }

    class ListenerThread extends Thread {
        private boolean goAhead = true;

        @Override
        public void run() {
            Log.d("Listener thread started for " + RemoteProxySocket.this.remoteConnection.getInetAddress());
            Event event;
            while (goAhead && RemoteProxySocket.this.remoteConnection.isConnected()) {
                try {
                    event = (Event) objectInputStream.readObject();
                    Log.d("Received " + event);
                    RemoteProxySocket.this.dispatchEventToRemoteView(event);
                } catch (IOException | ClassNotFoundException | ClassCastException e) {
                    Log.e("Invalid event received: " + e.getMessage());
                }
            }
        }

        void kill() {
            goAhead = false;
            try {
                objectInputStream.close();
            } catch (IOException e) {
                Log.e("Error during stram closing: " + e.getMessage());
            }
        }
    }

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

package it.polimi.se2018.view;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.viewEvent.ViewEvent;

/**
 * The main View. Communicates with the Model and the Controller.
 */
abstract public class View extends Observable<ViewEvent> implements Observer<Event> {

    private String player;

    private boolean connected = true;

    public View(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    /**
     * Checks if the user is still connected
     * @return true if it is, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Set the connection status of the user
     * @param connected true if the user is connected or false otherwise
     */
    protected void setConnected(boolean connected) {
        this.connected = connected;
    }
}

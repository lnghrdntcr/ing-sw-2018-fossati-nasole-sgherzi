package it.polimi.se2018.view;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.viewEvent.ViewEvent;

abstract public class View extends Observable<ViewEvent> implements Observer<Event> {

    private String player;

    private boolean connected = true;

    public View(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public boolean isConnected() {
        return connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }
}

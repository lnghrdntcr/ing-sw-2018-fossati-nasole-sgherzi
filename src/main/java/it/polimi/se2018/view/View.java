package it.polimi.se2018.view;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

abstract public class View extends Observable<Event> implements Observer<Event> {
    String player;

    public View(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}

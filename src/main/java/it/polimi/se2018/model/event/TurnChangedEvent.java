package it.polimi.se2018.model.event;

import it.polimi.se2018.utils.Event;

public class TurnChangedEvent extends Event {
    int round;

    public TurnChangedEvent(String emitter, String player, int round) {
        super(emitter, player);
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}

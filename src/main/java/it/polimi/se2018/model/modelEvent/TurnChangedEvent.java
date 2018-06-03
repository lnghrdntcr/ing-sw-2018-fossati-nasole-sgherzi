package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.utils.Event;

/**
 * Event to inform that the turn has changed
 */
public class TurnChangedEvent extends ModelEvent {
    int round;

    public TurnChangedEvent(String emitter, String player, int round) {
        super(emitter, player);
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}

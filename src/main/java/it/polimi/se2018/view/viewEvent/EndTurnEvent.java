package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;

public class EndTurnEvent extends Event {
    protected EndTurnEvent(String emitter, String player) {
        super(emitter, player);
    }
}

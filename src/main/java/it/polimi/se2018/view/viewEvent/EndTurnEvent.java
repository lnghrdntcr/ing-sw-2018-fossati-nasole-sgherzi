package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.utils.Event;

public class EndTurnEvent extends ViewEvent {
    public EndTurnEvent(String emitter, String player) {
        super(emitter, player);
    }

    @Override
    public State visit(State state) {
        return state.handleEndTurnEvent(this);
    }
}

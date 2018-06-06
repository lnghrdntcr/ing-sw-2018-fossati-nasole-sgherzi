package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;

public class PlayerDisconnectedEvent extends ViewEvent {
    public PlayerDisconnectedEvent(String emitter, String player) {
        super(emitter, player);
    }

    @Override
    public State visit(State state) {
        return state.handlePlayerDisconnected(this);
    }
}

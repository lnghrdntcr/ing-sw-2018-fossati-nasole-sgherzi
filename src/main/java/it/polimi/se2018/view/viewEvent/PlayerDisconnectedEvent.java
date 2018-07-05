package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;

public class PlayerDisconnectedEvent extends ViewEvent {
    public PlayerDisconnectedEvent(String emitter, String player, String receiver) {
        super(emitter, player, receiver);
    }

    public PlayerDisconnectedEvent(String json){
        super(json);
    }

    @Override
    public State visit(State state) {
        return state.handlePlayerDisconnected(this);
    }
}

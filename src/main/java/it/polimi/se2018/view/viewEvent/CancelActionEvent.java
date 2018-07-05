package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;

public class CancelActionEvent extends ViewEvent {

    public CancelActionEvent(String emitter, String player, String receiver) {
        super(emitter, player, receiver);
    }

    public CancelActionEvent(String json){
        super(json);
    }

    @Override
    public State visit(State state) {
        return state.handleUserCancelEvent();
    }
}

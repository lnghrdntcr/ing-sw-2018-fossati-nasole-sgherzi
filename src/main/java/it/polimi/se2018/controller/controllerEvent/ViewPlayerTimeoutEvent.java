package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.view.viewEvent.ViewEvent;

public class ViewPlayerTimeoutEvent extends ViewEvent {
    public ViewPlayerTimeoutEvent(String emitter, String receiver, String player) {
        super(emitter, receiver, player);
    }

    public ViewPlayerTimeoutEvent(String json){super(json);}

    @Override
    public State visit(State state) {
        return state.handleUserTimeOutEvent();
    }
}

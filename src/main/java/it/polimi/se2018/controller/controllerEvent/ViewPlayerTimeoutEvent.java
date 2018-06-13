package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.view.viewEvent.ViewEvent;

public class ViewPlayerTimeoutEvent extends ViewEvent {
    public ViewPlayerTimeoutEvent(String emitter, String player) {
        super(emitter, player);
    }

    @Override
    public State visit(State state) {
        return state.handleUserTimeOutEvent();
    }
}

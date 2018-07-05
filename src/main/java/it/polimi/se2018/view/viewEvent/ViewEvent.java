package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.utils.Event;

public abstract class ViewEvent extends Event {
    protected ViewEvent(String emitter, String player, String receiver) {
        super(emitter, player, receiver);
    }

    public ViewEvent(String json) {
        super(json);
    }

    abstract public State visit(State state);
}

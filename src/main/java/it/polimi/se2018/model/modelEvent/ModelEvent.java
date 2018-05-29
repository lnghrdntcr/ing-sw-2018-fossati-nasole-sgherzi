package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.utils.Event;

public abstract class ModelEvent extends Event {
    protected ModelEvent(String emitter, String player) {
        super(emitter, player);
    }
}

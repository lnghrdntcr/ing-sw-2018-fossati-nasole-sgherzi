package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.VisitableFromView;

/**
 * A generic model event
 */
public abstract class ModelEvent extends Event implements VisitableFromView{
    protected ModelEvent(String emitter, String player) {
        super(emitter, player);
    }
}

package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.Event;

public abstract class State {
    private Controller controller;

    public State(Controller controller) {
        this.controller = controller;
    }

    abstract public State handleEvent(Event event, GameTableMultiplayer model);

    public Controller getController() {
        return controller;
    }
}

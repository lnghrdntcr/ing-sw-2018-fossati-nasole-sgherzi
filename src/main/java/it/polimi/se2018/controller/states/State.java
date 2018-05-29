package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.utils.Event;

public abstract class State {
    private Controller controller;

    public State(Controller controller) {
        this.controller = controller;
    }

    abstract State handleEvent(Event event);

    public Controller getController() {
        return controller;
    }
}

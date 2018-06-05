package it.polimi.se2018.utils;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.model.GameTableMultiplayer;

import java.util.function.Function;

/**
 * Proposal as substitute for the event class
 * @since 03/06/2018
 */
public class ProposalForEvent {

    private String emitter;
    private String player;
    private Function<Tuple<Controller, GameTableMultiplayer>, State> reducer;

    public ProposalForEvent(String emitter, String player, Function<Tuple<Controller, GameTableMultiplayer>, State> f ){
        this.emitter = emitter;
        this.player = player;
        this.reducer = f;
    }

    /**
     * Calls the reducer function with controller and model as an argument.
     * @param controller The controller of the game.
     * @param model The model.
     * @return A new state, according to the logic provided by the reducer.
     */
    public State apply(Controller controller, GameTableMultiplayer model){
        return this.reducer.apply(new Tuple<>(controller, model));
    }

}

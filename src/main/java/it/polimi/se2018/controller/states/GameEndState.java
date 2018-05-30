package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.ScoreHolder;

import java.util.*;

/**
 * The state that handles the end of the game.
 * @since 29/05/2018
 */
public class GameEndState extends State {

    public GameEndState(Controller controller) {
        super(controller);
    }

    /**
     * The event handler for the EndGameEvent triggered.
     * @param event The EndGameEvent.
     * @param model The model of the game.
     * @return Null every time, as the game is up at this point.
     * @throws IllegalArgumentException If one of the argument (event, model) is null.
     * @throws IllegalStateException If event cannot be cast to an EndGameEvent, as this state should be reached only if a EndGameEvent is dispatched.
     * @throws IllegalStateException If there are no players on which compute the score.
     */
    @Override
    public State handleEvent(Event event, GameTableMultiplayer model) {

        if(event == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null.");
        if(model == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null.");
        if(!(event instanceof EndGameEvent)) throw new IllegalStateException(this.getClass().getCanonicalName() + ": You can only reach this state by dispatching an EndGameEvent");

        ArrayList<ScoreHolder> scoreHolders = model.computeAllScores();

        if(scoreHolders.isEmpty()) throw new IllegalStateException(this.getClass().getCanonicalName() + ": There are no players on which compute the score.");
        if(scoreHolders.size() != 1) Collections.sort(scoreHolders);

        this.getController().dispatchEvent(new EndGameEvent(this.getClass().getName(), null, scoreHolders));

        return null;

    }
}

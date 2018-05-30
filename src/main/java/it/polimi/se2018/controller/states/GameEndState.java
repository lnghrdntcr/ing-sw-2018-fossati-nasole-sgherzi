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

    public GameEndState(Controller controller, GameTableMultiplayer model) {
        super(controller);
        this.computeScore(model);
        // TODO: Add the logic to do other stuff eg. write things to file.
    }



    @Override
    public State handleEvent(Event event, GameTableMultiplayer model) {
        return null;
    }

     /**
     * The event handler for the EndGameEvent triggered.
     * @param model The model.
     * @return Null every time, as the game is up at this point.
     * @throws IllegalArgumentException If the model is null
     * @throws IllegalStateException If there are no players on which compute the score.
     */
    private void computeScore(GameTableMultiplayer model){

        if(model == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null.");

        ArrayList<ScoreHolder> scoreHolders = model.computeAllScores();

        if(scoreHolders.isEmpty()) throw new IllegalStateException(this.getClass().getCanonicalName() + ": There are no players on which compute the score.");
        if(scoreHolders.size() != 1) Collections.sort(scoreHolders);

        this.getController().dispatchEvent(new EndGameEvent(this.getClass().getName(), null, scoreHolders));

    }

}

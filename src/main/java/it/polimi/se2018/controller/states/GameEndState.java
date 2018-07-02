package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.ScoreHolder;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

import java.util.*;

/**
 * The state that handles the end of the game, computing the score and declaring the winner
 *
 * @since 29/05/2018
 */
public class GameEndState extends State {

    public GameEndState(Controller controller, GameTableMultiplayer model) {
        super(controller, model);
        controller.setGameEnded(true);
        this.computeScore();
        // TODO: Add the logic to do other stuff eg. write things to file.
        // TODO: Figure out a slightly better idea to end the game.
    }

    @Override
    public void syncPlayer(String playerName) {

    }

    /**
     * The event handler for the EndGameEvent triggered.
     *
     * @throws IllegalArgumentException If the model is null
     * @throws IllegalStateException    If there are no players on which compute the score.
     */
    private void computeScore() {

        if (getModel() == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null.");

        ArrayList<ScoreHolder> scoreHolders = getModel().computeAllScores();

        if (scoreHolders.isEmpty())
            throw new IllegalStateException(this.getClass().getCanonicalName() + ": There are no players on which compute the score.");
        if (scoreHolders.size() != 1) {
            Collections.sort(scoreHolders);
            Collections.reverse(scoreHolders);
        }


        scoreHolders.forEach(el -> {
            System.out.println(el);
        });

        this.getController().dispatchEvent(new EndGameEvent(this.getClass().getName(), "", "", scoreHolders));

    }

}

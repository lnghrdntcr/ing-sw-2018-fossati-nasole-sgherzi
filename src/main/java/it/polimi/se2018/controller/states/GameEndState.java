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
        this.computeScore();
        // TODO: Add the logic to do other stuff eg. write things to file.
        // TODO: Figure out a slightly better idea to end the game.
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
        if (scoreHolders.size() != 1) scoreHolders.sort(new WinnerComparator());

        Collections.reverse(scoreHolders);

        scoreHolders.forEach(el -> {
            System.out.println(el);
        });

        this.getController().dispatchEvent(new EndGameEvent(this.getClass().getName(), "", "", scoreHolders));

    }

    public class WinnerComparator implements Comparator<ScoreHolder> {

        @Override
        public int compare(ScoreHolder o1, ScoreHolder o2) {

            if (o1 == null || o2 == null) throw new NullPointerException();

            if (o1.equals(o2)) return 0;

            if (o1.getTotalScore() > o2.getTotalScore()) return 1;
            if (o1.getTotalScore() == o2.getTotalScore()) {
                if (o1.getPublicObjectivePoints() > o2.getPublicObjectivePoints()) return 1;
                if (o1.getPublicObjectivePoints() == o2.getPublicObjectivePoints()) {
                    if (o1.getTokenPoints() > o2.getTokenPoints()) return 1;
                    if (o1.getTokenPoints() == o2.getTokenPoints()) {
                        if (o1.getOrderInFinalRound() > o2.getOrderInFinalRound()) return 1;
                    }
                }
            }
            return -1;
        }
    }

}

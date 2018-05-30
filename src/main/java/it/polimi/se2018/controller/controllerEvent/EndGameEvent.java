package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.ScoreHolder;

import java.util.ArrayList;

/**
 * The event to be triggered at the end of a game.
 * @since 30/05/2018
 */
public class EndGameEvent extends ControllerEvent {

    private ArrayList<ScoreHolder> leaderBoard;

    public EndGameEvent(String emitter, String player, ArrayList<ScoreHolder> leaderBoard) {
        super(emitter, player);
        this.leaderBoard = leaderBoard;
    }

    public ArrayList<ScoreHolder> getLeaderBoard() {
        return leaderBoard;
    }
}

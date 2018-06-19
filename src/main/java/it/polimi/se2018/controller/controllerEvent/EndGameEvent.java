package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.ScoreHolder;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

import java.util.ArrayList;

/**
 * The event to be triggered at the end of a game.
 *
 * @since 30/05/2018
 */
public class EndGameEvent extends ControllerEvent {

    private ArrayList<ScoreHolder> leaderBoard;

    public EndGameEvent(String emitter, String receiver, String player, ArrayList<ScoreHolder> leaderBoard) {
        super(emitter, player, receiver);
        this.leaderBoard = leaderBoard;
    }

    public ArrayList<ScoreHolder> getLeaderBoard() {
        return leaderBoard;
    }

    @Override
    public void visit(GameTable gameTable) {
    }

    @Override
    public void visit(GameEnding gameEnding) {
        gameEnding.handleEndGameEvent(this);
    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}

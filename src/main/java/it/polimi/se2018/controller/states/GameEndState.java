package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.ScoreHolder;

import java.util.*;

public class GameEndState extends State {

    public GameEndState(Controller controller) {
        super(controller);
    }

    @Override
    public State handleEvent(Event event, GameTableMultiplayer model) {

        ArrayList<ScoreHolder> scoreHolders = model.computeAllScores();
        Collections.sort(scoreHolders);
        // AND THE WINNER IS...........
        System.out.println(scoreHolders.get(0).getPlayerName());

        return null;

    }
}

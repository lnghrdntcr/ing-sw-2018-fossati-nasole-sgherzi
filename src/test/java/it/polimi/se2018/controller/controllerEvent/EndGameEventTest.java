package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.ScoreHolder;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class EndGameEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        ArrayList<ScoreHolder> scoreHolders = new ArrayList<>();

        scoreHolders.add(new ScoreHolder("AAA", 12, 82, 7, 3, 2));
        scoreHolders.add(new ScoreHolder("BBB", 121, 821, 71, 31, 21));

        EndGameEvent event = new EndGameEvent("emitter", "receiver", "player", scoreHolders, "cia1");


        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

        assertEquals(event.getLeaderBoard().get(0).getTotalScore(), ((EndGameEvent) Event.decodeJSON(event.toJSON().toString())).getLeaderBoard().get(0).getTotalScore());
        assertEquals(event.getLeaderBoard().get(1).getTotalScore(), ((EndGameEvent) Event.decodeJSON(event.toJSON().toString())).getLeaderBoard().get(1).getTotalScore());
    }

}
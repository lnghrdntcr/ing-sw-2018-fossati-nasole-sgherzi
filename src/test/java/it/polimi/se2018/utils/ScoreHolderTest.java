package it.polimi.se2018.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ScoreHolderTest {

    ArrayList<ArrayList<ScoreHolder>> games = new ArrayList<>();

    @Before
    public void setUp() throws Exception {


        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            ArrayList<ScoreHolder> game = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                game.add(new ScoreHolder("Player" + j, j * 10, j, 2, 0, j));
            }

            games.add(game);
        }


    }

    @Test
    public void getTotalScore() {

        for (ArrayList<ScoreHolder> game : games){

            for (int i = 0; i < game.size(); i++) {

                ScoreHolder player = game.get(i);
                assertEquals(i*11 + 2, player.getTotalScore());

            }

        }

    }

    @Test
    public void getPlayerName() {

        for (ArrayList<ScoreHolder> game : games){

            for (int i = 0; i < game.size(); i++) {

                ScoreHolder player = game.get(i);
                assertEquals("Player" + i, player.getPlayerName());

            }

        }
    }

    @Test
    public void compareTo() {

        // Player one before player two, by points.
        ScoreHolder player1 = new ScoreHolder("Player1", 10, 10, 3, 0, 0);
        ScoreHolder player2 = new ScoreHolder("Player2", 4, 4, 1, 3, 1);

        assertEquals(1, player1.compareTo(player2));

        // Player one before player two, by public objective points.
        player1 = new ScoreHolder("Player1", 10, 10, 3, 0, 0);
        player2 = new ScoreHolder("Player2", 10, 7, 6, 0, 1);

        assertEquals(1, player1.compareTo(player2));

        // Player one before player two, by remaining tokens.
        player1 = new ScoreHolder("Player1", 10, 10, 7, 1, 0);
        player2 = new ScoreHolder("Player2", 10, 10, 6, 0, 1);

        assertEquals(1, player1.compareTo(player2));

        //Player one before player two, by order in round.
        player1 = new ScoreHolder("Player1", 10, 10, 6, 0, 1);
        player2 = new ScoreHolder("Player2", 10, 10, 6, 0, 0);
        assertEquals(1, player1.compareTo(player2));

        // Player two before player one, by any reason.
        player1 = new ScoreHolder("Player1", 10, 10, 6, 0, 1);
        player2 = new ScoreHolder("Player2", 20, 10, 6, 0, 0);
        assertEquals(-1, player1.compareTo(player2));

        // Testing properties that every compareTo function must have to comply with the spec:

        // `The natural ordering for a class C is said to be consistent with equals if and only
        // if e1.compareTo(e2) == 0
        // has the same boolean value as
        // e1.equals(e2) for every e1 and e2 of class C.
        // Note that null is not an instance of any class,
        // and e.compareTo(null) should throw a NullPointerException even though e.equals(null) returns false.`

        // Transitivity.
        assertEquals(-(player1.compareTo(player2)), player2.compareTo(player1));

        // Natural Ordering consistent with equals.
        assertEquals(0, player1.compareTo(player1));

        // NullPointerException if the input is null.
        try {
            player1.compareTo(null);
            fail();
        } catch (NullPointerException ignored){}

    }
}
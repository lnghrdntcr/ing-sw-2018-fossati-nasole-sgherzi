package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurnHolderTest {

    void nPlayers(int n){
        TurnHolder turnHolder = new TurnHolder(n);

        for(int i=0; i<10; i++){
            assertEquals(i, turnHolder.getRound());

            for(int j=0; j<2*n; j++){

                if (j < n) {
                    assertEquals(j, turnHolder.getCurrentPlayer());
                    assertTrue(turnHolder.isFirstTurnInRound());
                } else {
                    assertEquals(2*n-j-1, turnHolder.getCurrentPlayer());
                    assertFalse(turnHolder.isFirstTurnInRound());
                }

                turnHolder.nextTurn();

                if (!(i==9 && j==2*n-1)) {
                    assertFalse(turnHolder.isGameEnded());
                }else{
                    assertTrue(turnHolder.isGameEnded());
                }
            }
            assertEquals(i+1, turnHolder.getRound());

        }


        try{
            turnHolder.nextTurn();
            fail();
        }catch (IllegalStateException ignored){}

        assertTrue(turnHolder.isGameEnded());


    }

    @Test
    public void twoPlayers(){
        nPlayers(2);
    }

    @Test
    public void threePlayers(){
        nPlayers(3);
    }

    @Test
    public void fourPlayers(){
        nPlayers(4);
    }

    @Test
    public void wrongPlayers(){
        try{
            nPlayers(0);
            fail();
        }catch(IllegalArgumentException ignored){}

        try{
            nPlayers(1);
            fail();
        }catch(IllegalArgumentException ignored){}

        try{
            nPlayers(5);
            fail();
        }catch(IllegalArgumentException ignored){}

        try{
            nPlayers(29283);
            fail();
        }catch(IllegalArgumentException ignored){}
    }

}
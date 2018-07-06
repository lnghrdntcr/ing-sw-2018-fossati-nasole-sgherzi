package it.polimi.se2018.model;

import it.polimi.se2018.utils.Settings;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnHolderTest {

    void nPlayers(int n){
        ArrayList<Integer> playerList = new ArrayList<>();
        for(int i=0; i<n; i++){
            playerList.add(i);
        }

        TurnHolder turnHolder = new TurnHolder(n);

        for(int i = 0; i<Settings.TURNS; i++){
            assertEquals(i, turnHolder.getRound());

            for(int j=0; j<2*n; j++){



                assertEquals((int) playerList.get(j<n?j:2*n-j-1), turnHolder.getCurrentPlayer());
                System.out.println("Expected: "+ playerList.get(j<n?j:2*n-j-1) + " got: "+ turnHolder.getCurrentPlayer());

                turnHolder.nextTurn();

                if (!(i==Settings.TURNS-1 && j==2*n-1)) {
                    assertFalse(turnHolder.isGameEnded());
                }else{
                    assertTrue(turnHolder.isGameEnded());
                }
            }
            assertEquals(i+1, turnHolder.getRound());
            playerList.add(playerList.remove(0));
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
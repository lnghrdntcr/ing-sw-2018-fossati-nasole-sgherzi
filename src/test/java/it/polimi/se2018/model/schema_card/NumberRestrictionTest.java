package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberRestrictionTest {
    @Test
    public void generalTest(){
        for(int i=1; i<=6; i++){
            NumberRestriction numberRestriction = new NumberRestriction(i);
            for(GameColor gc : GameColor.values()){
                for(int j=1; j<=6; j++){
                    if(i==j){
                        assertTrue(numberRestriction.isDiceAllowed(new DiceFace(gc, j)));
                    }else{
                        assertFalse(numberRestriction.isDiceAllowed(new DiceFace(gc, j)));
                    }
                }
            }
        }
    }

    @Test
    public void wrongNumberTest(){
        try{
            new NumberRestriction(0);
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            new NumberRestriction(-1);
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            new NumberRestriction(7);
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            new NumberRestriction(8);
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            new NumberRestriction(846521);
            fail();
        }catch (IllegalArgumentException ignored){}
    }

}
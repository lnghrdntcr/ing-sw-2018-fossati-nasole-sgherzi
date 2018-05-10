package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoRestrictionTest {
    @Test
    public void generalTest(){
        NoRestriction restriction = new NoRestriction();
        for(GameColor gc : GameColor.values()){
            for(int i=1; i<=6; i++){
                assertTrue(restriction.isDiceAllowed(new DiceFace(gc, i)));
            }
        }
    }

}
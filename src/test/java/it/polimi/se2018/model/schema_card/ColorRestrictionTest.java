package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColorRestrictionTest {

    @Test
    public void generalTest(){
        for(GameColor gc : GameColor.values()){
            ColorRestriction colorRestriction = new ColorRestriction(gc);
            for(GameColor gc2 : GameColor.values()){
                for(int i =1; i<=6; i++){
                    if(gc.equals(gc2)){
                        assertTrue(colorRestriction.isDiceAllowed(new DiceFace(gc2, i)));
                    }else{
                        assertFalse(colorRestriction.isDiceAllowed(new DiceFace(gc2, i)));
                    }
                }
            }
        }

    }

}
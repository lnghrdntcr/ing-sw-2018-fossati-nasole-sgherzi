package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.NO_IMPLEMENT;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaCardFaceTest {
    static SchemaCardFace front;
    @BeforeClass
    public static void pre() throws FileNotFoundException {
        List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/validTest1.scf");

        assertEquals(1, schemaCardList.size());

        front = schemaCardList.get(0).getFace(Side.FRONT);
    }


    @Test
    public void failureTest() {
        try{
            front.getRestriction(new Point(-1, -2));
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            front.isDiceAllowed(new Point(0, 0), null, SchemaCardFace.Ignore.NOTHING);
            fail();
        }catch (IllegalArgumentException ignored){}
    }

    @Test
    public void normalTest(){
        assertFalse(front.isDiceAllowed(new Point(0, 0), new DiceFace(GameColor.RED, 5), SchemaCardFace.Ignore.NOTHING));
        assertTrue(front.isDiceAllowed(new Point(0, 0), new DiceFace(GameColor.RED, 4), SchemaCardFace.Ignore.NOTHING));
    }

}
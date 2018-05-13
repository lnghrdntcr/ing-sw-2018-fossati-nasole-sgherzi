package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.GameColor;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaCardTest {

    @Test
    public void testValid() throws FileNotFoundException {
        List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/validTest1.scf");

        assertEquals(1, schemaCardList.size());

        SchemaCardFace front = schemaCardList.get(0).getFace(Side.FRONT);

        //schema infos
        assertEquals(5, front.getDifficulty());
        assertEquals("Virtus", front.getName());

        //numbers
        assertEquals(4, ((NumberRestriction)front.getRestriction(new Point(0, 0))).getNumber());
        assertEquals(2, ((NumberRestriction)front.getRestriction(new Point(2, 0))).getNumber());
        assertEquals(5, ((NumberRestriction)front.getRestriction(new Point(3, 0))).getNumber());
        assertEquals(6, ((NumberRestriction)front.getRestriction(new Point(2, 1))).getNumber());
        assertEquals(2, ((NumberRestriction)front.getRestriction(new Point(4, 1))).getNumber());
        assertEquals(3, ((NumberRestriction)front.getRestriction(new Point(1, 2))).getNumber());
        assertEquals(4, ((NumberRestriction)front.getRestriction(new Point(3, 2))).getNumber());
        assertEquals(5, ((NumberRestriction)front.getRestriction(new Point(0, 3))).getNumber());
        assertEquals(1, ((NumberRestriction)front.getRestriction(new Point(2, 3))).getNumber());

        //Color restrictions
        assertEquals(GameColor.GREEN, ((ColorRestriction)front.getRestriction(new Point(4, 0))).getColor());
        assertEquals(GameColor.GREEN, ((ColorRestriction)front.getRestriction(new Point(3, 1))).getColor());
        assertEquals(GameColor.GREEN, ((ColorRestriction)front.getRestriction(new Point(2, 2))).getColor());
        assertEquals(GameColor.GREEN, ((ColorRestriction)front.getRestriction(new Point(1, 3))).getColor());

        //No restrictions
        assertTrue(front.getRestriction(new Point(1, 0)) instanceof NoRestriction);
        assertTrue(front.getRestriction(new Point(0, 1)) instanceof NoRestriction);
        assertTrue(front.getRestriction(new Point(1, 1)) instanceof NoRestriction);
        assertTrue(front.getRestriction(new Point(0, 2)) instanceof NoRestriction);
        assertTrue(front.getRestriction(new Point(4, 2)) instanceof NoRestriction);
        assertTrue(front.getRestriction(new Point(3, 3)) instanceof NoRestriction);
        assertTrue(front.getRestriction(new Point(4, 3)) instanceof NoRestriction);
    }

    @Test
    public void testInvalid1() throws FileNotFoundException {
        List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/invalidTest_wrongface.scf");

        assertEquals(0, schemaCardList.size());

    }

    @Test
    public void noFile(){
        try{
            List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/thisfileshouldnotexist.scf");
            fail();
        } catch (FileNotFoundException ignored) {

        }
    }

    @Test
    public void testInvalid2() throws FileNotFoundException {
        List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/invalidTest_wrongjson1.scf");

        assertEquals(0, schemaCardList.size());

    }


    @Test
    public void testInvalid3() throws FileNotFoundException {
        List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/invalidTest_wrongjson2.scf");

        assertEquals(0, schemaCardList.size());

    }

    @Test
    public void testInvalid4() throws FileNotFoundException {
        List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/invalidTest_wrongdim1.scf");

        assertEquals(0, schemaCardList.size());

    }

    @Test
    public void testInvalid5() throws FileNotFoundException {
        List<SchemaCard> schemaCardList = SchemaCard.loadSchemaCardsFromJson("./gameData/tests/invalidTest_wrongdim2.scf");

        assertEquals(0, schemaCardList.size());

    }
}
package it.polimi.se2018.model.schema;

import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class SchemaTest {
    private Schema virtus, empty;

    @Before
    public void setUp() throws FileNotFoundException {
        SchemaCardFace front = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_EqualCards.scf").get(0).getFace(Side.FRONT);
         virtus = new Schema(front);

         front = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);
         empty = new Schema(front);
    }

    @Test
    public void constructor(){
        try{
            new Schema(null);
            fail();
        }catch (IllegalArgumentException ignored){}
    }


    @Test
    public void getDiceFace() {
        assertNull(virtus.getDiceFace(new Point(0, 0)));
        assertNull(virtus.getDiceFace(new Point(0, 0)));

        try{
            virtus.getDiceFace(null);
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            virtus.getDiceFace(new Point(-1, 2));
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            virtus.getDiceFace( new Point(4, 5));
            fail();
        }catch (IllegalArgumentException ignored){}
    }

    @Test
    public void setDiceFace() {
        virtus.setDiceFace(new Point(1, 3), new DiceFace(GameColor.RED, 2));
        empty.setDiceFace(new Point(3, 1), new DiceFace(GameColor.BLUE, 1));

        assertEquals(2, virtus.getDiceFace(new Point(1, 3)).getNumber());
        assertEquals(GameColor.RED, virtus.getDiceFace(new Point(1, 3)).getColor());

        assertEquals(1, empty.getDiceFace(new Point(3, 1)).getNumber());
        assertEquals(GameColor.BLUE, empty.getDiceFace(new Point(3, 1)).getColor());

        assertNull(virtus.getDiceFace(new Point(0, 0)));
        virtus.setDiceFace(new Point(1, 3), null);
        assertNull(virtus.getDiceFace(new Point(1, 3)));

        try{
            virtus.setDiceFace(new Point(-1, 0), new DiceFace(GameColor.RED, 2));
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            virtus.setDiceFace(new Point(5, 4), new DiceFace(GameColor.RED, 2));
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            virtus.setDiceFace(null, new DiceFace(GameColor.RED, 2));
            fail();
        }catch (IllegalArgumentException ignored){}
    }

    @Test
    public void isDiceAllowed() {
        assertFalse(virtus.isDiceAllowed(new Point(1, 1), new DiceFace(GameColor.RED, 1)));
        assertFalse(empty.isDiceAllowed(new Point(1, 1), new DiceFace(GameColor.RED, 1)));

        assertTrue(virtus.isDiceAllowed(new Point(0, 0), new DiceFace(GameColor.RED, 4)));
        assertTrue(empty.isDiceAllowed(new Point(0, 0), new DiceFace(GameColor.RED, 4)));

        virtus.setDiceFace(new Point(0, 0), new DiceFace(GameColor.RED, 4));
        empty.setDiceFace(new Point(0, 0), new DiceFace(GameColor.RED, 4));

        assertTrue(virtus.isDiceAllowed(new Point(1, 1), new DiceFace(GameColor.RED, 1)));
        assertTrue(empty.isDiceAllowed(new Point(1, 1), new DiceFace(GameColor.RED, 1)));

        assertTrue(virtus.isDiceAllowed(new Point(1, 0), new DiceFace(GameColor.BLUE, 1)));
        assertTrue(empty.isDiceAllowed(new Point(1, 0), new DiceFace(GameColor.BLUE, 1)));

        assertFalse(virtus.isDiceAllowed(new Point(1, 0), new DiceFace(GameColor.RED, 1)));
        assertFalse(empty.isDiceAllowed(new Point(1, 0), new DiceFace(GameColor.RED, 1)));

        assertFalse(virtus.isDiceAllowed(new Point(2, 2), new DiceFace(GameColor.GREEN, 1)));
        assertFalse(empty.isDiceAllowed(new Point(2, 2), new DiceFace(GameColor.GREEN, 1)));


        //Ignoring validity for tests
        virtus.setDiceFace(new Point(2, 2), new DiceFace(GameColor.RED, 1));
        empty.setDiceFace(new Point(2, 2), new DiceFace(GameColor.RED, 1));

        assertFalse(virtus.isDiceAllowed(new Point(1, 2), new DiceFace(GameColor.RED, 1)));
        assertFalse(empty.isDiceAllowed(new Point(1, 2), new DiceFace(GameColor.RED, 1)));

        assertFalse(virtus.isDiceAllowed(new Point(3, 2), new DiceFace(GameColor.RED, 1)));
        assertFalse(empty.isDiceAllowed(new Point(3, 2), new DiceFace(GameColor.RED, 1)));

        assertFalse(virtus.isDiceAllowed(new Point(2, 1), new DiceFace(GameColor.RED, 1)));
        assertFalse(empty.isDiceAllowed(new Point(2, 1), new DiceFace(GameColor.RED, 1)));

        assertFalse(virtus.isDiceAllowed(new Point(2, 3), new DiceFace(GameColor.RED, 1)));
        assertFalse(empty.isDiceAllowed(new Point(2, 3), new DiceFace(GameColor.RED, 1)));

        try{
            empty.isDiceAllowed(null, new DiceFace(GameColor.RED, 1));
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            empty.isDiceAllowed(new Point(1, 0), null);
            fail();
        }catch (IllegalArgumentException ignored){}

        try{
            empty.isDiceAllowed(new Point(1, -1), new DiceFace(GameColor.RED, 2));
            fail();
        }catch (IllegalArgumentException ignored){}
    }

    @Test
    public void isEmpty() {
        assertTrue(virtus.isEmpty());
        assertTrue(empty.isEmpty());

        virtus.setDiceFace(new Point(1, 3), new DiceFace(GameColor.RED, 2));
        empty.setDiceFace(new Point(3, 1), new DiceFace(GameColor.BLUE, 1));

        assertFalse(virtus.isEmpty());
        assertFalse(empty.isEmpty());
    }

    @Test
    public void getSchemaCardFace() {
        assertTrue(virtus.getSchemaCardFace().getName().equalsIgnoreCase("virtus"));
        assertTrue(empty.getSchemaCardFace().getName().equalsIgnoreCase("empty front"));
    }
}
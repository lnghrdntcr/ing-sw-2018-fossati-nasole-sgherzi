package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.GameColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellRestrictionTest {
    @Test
    public void normalRestrictions(){
        //NumberRestriction
        assertEquals(1, ((NumberRestriction)CellRestriction.getRestrictionFromString("1")).getNumber());
        assertEquals(2, ((NumberRestriction)CellRestriction.getRestrictionFromString("2")).getNumber());
        assertEquals(3, ((NumberRestriction)CellRestriction.getRestrictionFromString("3")).getNumber());
        assertEquals(4, ((NumberRestriction)CellRestriction.getRestrictionFromString("4")).getNumber());
        assertEquals(5, ((NumberRestriction)CellRestriction.getRestrictionFromString("5")).getNumber());
        assertEquals(6, ((NumberRestriction)CellRestriction.getRestrictionFromString("6")).getNumber());

        //NoRestriction
        assertTrue(CellRestriction.getRestrictionFromString("") instanceof NoRestriction);

        //ColorRestriction
        assertEquals(GameColor.RED, ((ColorRestriction)CellRestriction.getRestrictionFromString("RED")).getColor());
        assertEquals(GameColor.BLUE, ((ColorRestriction)CellRestriction.getRestrictionFromString("BLUE")).getColor());
        assertEquals(GameColor.YELLOW, ((ColorRestriction)CellRestriction.getRestrictionFromString("YELLOW")).getColor());
        assertEquals(GameColor.GREEN, ((ColorRestriction)CellRestriction.getRestrictionFromString("GREEN")).getColor());
        assertEquals(GameColor.PURPLE, ((ColorRestriction)CellRestriction.getRestrictionFromString("PURPLE")).getColor());

    }

    @Test
    public void invalidCases() {
        try {
            CellRestriction.getRestrictionFromString("PAPUNNO");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            CellRestriction.getRestrictionFromString("7");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            CellRestriction.getRestrictionFromString("0");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            CellRestriction.getRestrictionFromString("cosa c'è scritto qui? Non lo so esattamente D:");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            CellRestriction.getRestrictionFromString("28934728957483475839875289384723897482752837548923748273562835742389478923478728395782374");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            CellRestriction.getRestrictionFromString(null);
            fail();
        } catch (IllegalArgumentException ignored) {}

    }

}
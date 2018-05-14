package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class PlayerTest {
    final String name="Nico";
    private Player nico;
    private Schema schema;
    @Before
    public void setUp() throws FileNotFoundException {
        SchemaCardFace cardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);
        schema = new Schema(cardFace);

        nico=new Player(name);
    }

    @Test
    public void getSchema() {
        assertNull(nico.getSchema());
    }

    @Test
    public void setSchema() {
        nico.setSchema(schema);

        assertSame(schema, nico.getSchema());
    }

    @Test
    public void getName() {
        assertEquals(name, nico.getName());
    }
}
package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.utils.Settings;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class PlayerTest {
    final String name = "Nico";
    private Player nico;
    private Schema schema;
    private SchemaCardFace scf;

    @Before
    public void setUp() throws FileNotFoundException {
        this.scf = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);
        schema = new Schema(scf);
        nico = new Player(name);
        this.nico.setToken(scf.getDifficulty());
        this.nico.setPrivateObjective(new PrivateObjective(GameColor.RED));
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
    public void computeScoreFromPrivateObjective() throws FileNotFoundException {

        this.nico.setSchema(this.schema);



        for (GameColor gc : GameColor.values()) {

            int actualPoints = 0;
            Player player = new Player("pippo");
            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);
            player.setSchema(new Schema(schemaCardFace));
            player.setPrivateObjective(new PrivateObjective(gc));

            for (int x = 0; x < Settings.CARD_WIDTH; x++) {
                for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                    assertEquals(actualPoints, player.computeScoreFromPrivateObjective());
                    player.getSchema().setDiceFace(new Point(x, y), new DiceFace(gc, x * y % 6 + 1));
                    actualPoints += (x * y) % 6 + 1;
                }
            }

        }
    }

    @Test
    public void computeFreeSpaces() {

        this.nico.setSchema(this.schema);

        int actualFreeSpaces = Settings.CARD_HEIGHT * Settings.CARD_WIDTH;

        for (int x = 0; x < Settings.CARD_WIDTH; x++) {
            for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                assertEquals(actualFreeSpaces, this.nico.computeFreeSpaces());
                this.nico.getSchema().setDiceFace(new Point(x, y), new DiceFace(GameColor.RED, 1));
                actualFreeSpaces--;
            }
        }

    }

    @Test
    public void getImmutableInstance() {

        PlayerImmutable nicoImmutable = this.nico.getImmutableInstance();

        assertEquals(nicoImmutable.getName(), this.nico.getName());
        assertEquals(nicoImmutable.getPrivateObjective(), this.nico.getPrivateObjective());
        assertEquals(nicoImmutable.getToken(), this.nico.getToken());

    }
}
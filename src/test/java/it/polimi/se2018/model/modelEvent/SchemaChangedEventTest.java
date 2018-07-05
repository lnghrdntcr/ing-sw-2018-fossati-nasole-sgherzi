package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Settings;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaChangedEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException, FileNotFoundException {
        List<SchemaCard> schemaCards = SchemaCard.loadSchemaCardsFromJson(Settings.getDefaultSchemaCardDatabase());

        Schema schema = new Schema(schemaCards.get(0).getFace(Side.FRONT));

        schema.setDiceFace(new Point(0, 0), new DiceFace(GameColor.BLUE, 3));
        schema.setDiceFace(new Point(1, 3), new DiceFace(GameColor.RED, 2));

        SchemaChangedEvent event = new SchemaChangedEvent("emitter", "receiver", "player", schema);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

    }

}
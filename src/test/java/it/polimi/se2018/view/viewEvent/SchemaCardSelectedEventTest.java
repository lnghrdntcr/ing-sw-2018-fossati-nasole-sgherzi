package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchemaCardSelectedEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        SchemaCardSelectedEvent event = new SchemaCardSelectedEvent("emitter", "receiver", "player", 1, Side.FRONT);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

        event = new SchemaCardSelectedEvent("emitter", "receiver", "player", 0, Side.BACK);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }
}
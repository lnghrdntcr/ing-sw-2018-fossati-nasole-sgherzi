package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.Tool;
import it.polimi.se2018.model.objectives.ColorVariety;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class ToolCardChangedEventTest {


    @Test
    public void serializationTest() throws ClassNotFoundException {
        Tool toolCard = new Tool("PROVAPROVAPROVA");
        toolCard.addToken(4);
        ToolCardChangedEvent event = new ToolCardChangedEvent("emitter", "receiver", "player", toolCard.getImmutableInstance(), 7);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

    }

}
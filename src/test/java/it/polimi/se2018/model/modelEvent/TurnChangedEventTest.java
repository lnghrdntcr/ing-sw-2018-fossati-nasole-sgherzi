package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.objectives.ColorVariety;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurnChangedEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        TurnChangedEvent event = new TurnChangedEvent("emitter", "receiver", "player", 7, true, true, true);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

        event = new TurnChangedEvent("emitter", "receiver", "player", 7, false, false, false);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

    }

}
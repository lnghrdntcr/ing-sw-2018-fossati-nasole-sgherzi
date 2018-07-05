package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class AskPlaceRedrawDiceEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        AskPlaceRedrawDiceEvent event = new AskPlaceRedrawDiceEvent("emitter", "receiver", "player", 0);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

        event = new AskPlaceRedrawDiceEvent("emitter", "receiver", "player", 234);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }

}
package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameStartEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        GameStartEvent event = new GameStartEvent("emitter", "receiver", "player");

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

    }

}
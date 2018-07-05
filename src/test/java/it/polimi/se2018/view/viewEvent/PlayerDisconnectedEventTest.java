package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerDisconnectedEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        PlayerDisconnectedEvent event = new PlayerDisconnectedEvent("emitter", "receiver", "player");

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
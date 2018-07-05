package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeoutCommunicationEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        TimeoutCommunicationEvent event = new TimeoutCommunicationEvent("emitter", "receiver", "player", 7);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }

}
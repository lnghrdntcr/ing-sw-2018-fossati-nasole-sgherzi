package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        LogEvent event = new LogEvent("emitter", "receiver", "player", "This is a message and should not trigger any error %$///\\||^%\"" );

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }

}
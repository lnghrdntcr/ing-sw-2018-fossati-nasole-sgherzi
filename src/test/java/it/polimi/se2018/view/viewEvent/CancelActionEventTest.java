package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class CancelActionEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        CancelActionEvent event = new CancelActionEvent("emitter", "player", "receiver");

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
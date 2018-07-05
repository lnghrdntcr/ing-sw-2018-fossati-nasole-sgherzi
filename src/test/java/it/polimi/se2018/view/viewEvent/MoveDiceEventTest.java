package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class MoveDiceEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        MoveDiceEvent event = new MoveDiceEvent("emitter", "receiver", "player", 5, new Point(2, 3), new Point(7, 8));

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
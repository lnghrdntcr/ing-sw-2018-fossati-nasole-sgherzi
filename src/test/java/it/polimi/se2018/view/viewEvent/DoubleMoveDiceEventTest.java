package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class DoubleMoveDiceEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        DoubleMoveDiceEvent event = new DoubleMoveDiceEvent("emitter", "receiver", "player", 1, new Point(3, 4), new Point(5, 6), new Point(7, 8), new Point(9, 0));

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
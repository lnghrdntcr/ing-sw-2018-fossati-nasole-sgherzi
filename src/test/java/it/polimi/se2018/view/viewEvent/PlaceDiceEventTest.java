package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlaceDiceEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        PlaceDiceEvent event = new PlaceDiceEvent("emitter", "receiver", "player", 4, new Point(8, 9));

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }

}
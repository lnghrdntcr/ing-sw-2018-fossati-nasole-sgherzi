package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlaceAnotherDiceEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        PlaceAnotherDiceEvent event = new PlaceAnotherDiceEvent("emitter", "receiver", "player", 7, new Point(2, 3), 6);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
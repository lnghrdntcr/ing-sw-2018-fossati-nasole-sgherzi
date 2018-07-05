package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlaceAnotherDiceSelectingNumberEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        PlaceAnotherDiceSelectingNumberEvent event = new PlaceAnotherDiceSelectingNumberEvent("emitter", "receiver", "player", 2, new Point(3, 4), 5, 6);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
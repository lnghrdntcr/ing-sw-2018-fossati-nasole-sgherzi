package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class AskPlaceRedrawDiceWithNumberSelectionEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        AskPlaceRedrawDiceWithNumberSelectionEvent event = new AskPlaceRedrawDiceWithNumberSelectionEvent("emitter", "receiver", "player", 0);
        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

        event = new AskPlaceRedrawDiceWithNumberSelectionEvent("emitter", "receiver", "player", 43);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }

}

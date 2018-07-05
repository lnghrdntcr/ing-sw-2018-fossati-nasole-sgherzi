package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ChangeDiceNumberEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        ChangeDiceNumberEvent event = new ChangeDiceNumberEvent("emitter", "receiver", "player", 2, 3, 4);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }
}
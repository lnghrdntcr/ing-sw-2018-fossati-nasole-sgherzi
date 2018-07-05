package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class DiceActionEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        DiceActionEvent event = new DiceActionEvent("emitter", "receiver", "player", 4, 6);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }
}
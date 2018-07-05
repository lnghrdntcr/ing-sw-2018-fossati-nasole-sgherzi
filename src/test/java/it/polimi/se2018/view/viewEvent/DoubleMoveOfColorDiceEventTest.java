package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class DoubleMoveOfColorDiceEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        DoubleMoveOfColorDiceEvent event = new DoubleMoveOfColorDiceEvent("emitter", "receiver", "player", 2, new Point(1, 2), new Point(3, 4), new Point(5, 6), new Point(7, 8), GameColor.RED);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


        event = new DoubleMoveOfColorDiceEvent("emitter", "receiver", "player", 2, new Point(1, 2), new Point(3, 4), new Point(5, 6), new Point(7, 8), GameColor.GREEN);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
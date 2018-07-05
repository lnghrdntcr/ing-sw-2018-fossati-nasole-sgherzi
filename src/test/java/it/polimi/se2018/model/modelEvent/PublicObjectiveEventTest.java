package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.objectives.ColorVariety;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PublicObjectiveEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        PublicObjectiveEvent event = new PublicObjectiveEvent("emitter", "receiver", "player", new ColorVariety(), 2);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

    }

}
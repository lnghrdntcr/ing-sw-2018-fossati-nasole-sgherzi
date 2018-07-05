package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.modelEvent.TurnChangedEvent;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class SwapDiceFaceWithDiceHolderEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        SwapDiceFaceWithDiceHolderEvent event = new SwapDiceFaceWithDiceHolderEvent("emitter", "receiver", "player", 2, 3, 4, 5);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());


    }
}
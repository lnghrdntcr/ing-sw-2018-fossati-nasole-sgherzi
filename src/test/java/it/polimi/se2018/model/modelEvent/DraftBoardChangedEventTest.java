package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.DraftBoard;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class DraftBoardChangedEventTest {
    @Test
    public void serializationTest() throws ClassNotFoundException {
        DraftBoard draftBoard = new DraftBoard();

        draftBoard.drawDices(3);

        DraftBoardChangedEvent event = new DraftBoardChangedEvent("emitter", "receiver", "player", draftBoard.getImmutableInstance());

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
        DraftBoardChangedEvent reconstructedEvent = (DraftBoardChangedEvent) Event.decodeJSON(event.toJSON().toString());

        for(int i=0; i<event.getDraftBoardImmutable().getDices().length; i++){
            assertEquals(event.getDraftBoardImmutable().getDices()[i].getColor(), reconstructedEvent.getDraftBoardImmutable().getDices()[i].getColor());
            assertEquals(event.getDraftBoardImmutable().getDices()[i].getNumber(), reconstructedEvent.getDraftBoardImmutable().getDices()[i].getNumber());
        }
    }
}
package it.polimi.se2018.utils;

import it.polimi.se2018.view.viewEvent.CancelActionEvent;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {
    @Test
    public void decodeErrorInvalidJson(){
        try{
            Event.decodeJSON("This is not JSON!!!oneone111!");
            fail();
        } catch (Exception ignored) {
        }
    }


    @Test
    public void decodeErrorInvalidClass(){
        try{
            Event.decodeJSON("{\"receiver\":\"player\",\"playerName\":\"receiver\",\"emitterName\":\"emitter\",\"class\":\"it.polimi.se2018.model.modelEvent.PlayerChangedEvent\"}");
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void decodeErrorInvalidParams(){
        try{
            Event.decodeJSON("{\"receiverDoesNotExist\":\"player\",\"playerName\":\"receiver\",\"emitterName\":\"emitter\",\"class\":\"it.polimi.se2018.thisclassdoesnotexist\"}");
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testFilter(){
        CancelActionEvent event = new CancelActionEvent("a", "b", "c");
        assertSame(event, event.filter("someplayer"));
    }
}
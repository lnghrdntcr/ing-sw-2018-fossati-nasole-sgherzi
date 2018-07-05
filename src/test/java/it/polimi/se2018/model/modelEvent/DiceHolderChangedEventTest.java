package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.controller.controllerEvent.ViewPlayerTimeoutEvent;
import it.polimi.se2018.model.DiceHolder;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Settings;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class DiceHolderChangedEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        DiceHolder diceHolder = new DiceHolder();

        for(int i=0; i<Settings.TURNS; i++){
            for(int x=0; x<5; x++){
                diceHolder.addDice(i, new DiceFace(GameColor.values()[new Random().nextInt(GameColor.values().length)], new Random().nextInt(6)+1));
            }
        }


        DiceHolderChangedEvent event = new DiceHolderChangedEvent("emitter", "receiver", "player", diceHolder.getImmutableInstance());

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

        DiceHolderChangedEvent reconstructedEvent = (DiceHolderChangedEvent)  Event.decodeJSON(event.toJSON().toString());

        for(int i=0; i<Settings.TURNS; i++){
            for(int x=0; x<event.getDiceHolderImmutable().getDiceFaces(i).length; x++){
                assertTrue(event.getDiceHolderImmutable().getDiceFaces(i)[x].getNumber()==(reconstructedEvent.getDiceHolderImmutable().getDiceFaces(i)[x]).getNumber());
                assertTrue(event.getDiceHolderImmutable().getDiceFaces(i)[x].getColor()==(reconstructedEvent.getDiceHolderImmutable().getDiceFaces(i)[x]).getColor());
            }
        }
    }

}
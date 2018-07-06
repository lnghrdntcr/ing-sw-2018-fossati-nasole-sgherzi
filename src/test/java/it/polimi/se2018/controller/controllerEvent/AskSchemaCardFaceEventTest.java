package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Settings;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class AskSchemaCardFaceEventTest {

    @Test
    public void serializationTest() throws FileNotFoundException, ClassNotFoundException {
        List<SchemaCard> cardList = SchemaCard.loadSchemaCardsFromJson("gameData/tests/schemaCards/schemaCardBase.scf");
        Collections.shuffle(cardList);

        SchemaCard array[]= new SchemaCard[2];
        array[0]=cardList.get(0);
        array[1]=cardList.get(1);

        AskSchemaCardFaceEvent event = new AskSchemaCardFaceEvent("emitter", "receiver", "player", array);

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());
    }

}
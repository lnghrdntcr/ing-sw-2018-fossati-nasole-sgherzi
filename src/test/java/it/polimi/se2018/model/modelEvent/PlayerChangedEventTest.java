package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.utils.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerChangedEventTest {

    @Test
    public void serializationTest() throws ClassNotFoundException {
        Player player=new Player("player12345");
        player.setPrivateObjective(new PrivateObjective(GameColor.RED));
        player.setToken(7);

        PlayerChangedEvent event = new PlayerChangedEvent("emitter", "receiver", "player", player.getImmutableInstance());

        assertEquals(event.toJSON().toString(), Event.decodeJSON(event.toJSON().toString()).toJSON().toString());

    }


    @Test
    public void filter(){
        Player player=new Player("player12345");
        player.setPrivateObjective(new PrivateObjective(GameColor.RED));
        player.setToken(7);

        PlayerChangedEvent event = new PlayerChangedEvent("emitter", "receiver", "player", player.getImmutableInstance());

        assertNull(event.filter("otherPlayerName").getPlayerImmutable().getPrivateObjective());
        assertNotNull(event.filter("player12345").getPlayerImmutable().getPrivateObjective());
    }

}
package it.polimi.se2018.model.event;

import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.utils.Event;

public class PlayerChangedEvent extends Event {
    PlayerImmutable playerImmutable;

    public PlayerChangedEvent(String emitter, String player, PlayerImmutable playerImmutable) {
        super(emitter, player);
        this.playerImmutable = playerImmutable;
    }

    public PlayerImmutable getPlayerImmutable() {
        return playerImmutable;
    }
}

package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

import java.io.Serializable;

/**
 * Event that is used to inform that a payer has been changed
 */
public class PlayerChangedEvent extends ModelEvent {
    PlayerImmutable playerImmutable;

    public PlayerChangedEvent(String emitter, String receiver, String player, PlayerImmutable playerImmutable) {
        super(emitter, player, receiver);
        this.playerImmutable = playerImmutable;
    }

    public PlayerImmutable getPlayerImmutable() {
        return playerImmutable;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handlePlayerChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {
        selectSchemaCardFace.handlePlayerCanged(this);
    }

    @Override
    public Event filter(String playername) {
        if(!playername.equals(playerImmutable.getName())){
            return new PlayerChangedEvent(getEmitterName(), getReceiver(), getPlayerName(), playerImmutable.getFilteredInstance());
        }
        return this;
    }
}

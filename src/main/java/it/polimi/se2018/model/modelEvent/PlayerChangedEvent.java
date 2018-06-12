package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * Event that is used to inform that a payer has been changed
 */
public class PlayerChangedEvent extends ModelEvent {
    PlayerImmutable playerImmutable;

    public PlayerChangedEvent(String emitter, String player, PlayerImmutable playerImmutable) {
        super(emitter, player);
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

    }
}

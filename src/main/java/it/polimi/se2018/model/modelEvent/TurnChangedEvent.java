package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * Event to inform that the turn has changed
 */
public class TurnChangedEvent extends ModelEvent {
    private int round;
    private boolean direction;

    public TurnChangedEvent(String emitter, String receiver, String player, int round, boolean direction) {
        super(emitter, player, receiver);
        this.round = round;
        this.direction = direction;
    }

    public int getRound() {
        return round;
    }

    public boolean getDirection() {
        return direction;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleTurnChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}

package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectScheaCardFace;

/**
 * Event to inform that the turn has changed
 */
public class TurnChangedEvent extends ModelEvent {
    private int round;
    private int direction;

    public TurnChangedEvent(String emitter, String player, int round, int direction) {
        super(emitter, player);
        this.round = round;
        this.direction = direction;
    }

    public int getRound() {
        return round;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public void visit(GameTable gameTable) {

    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectScheaCardFace selectScheaCardFace) {

    }
}

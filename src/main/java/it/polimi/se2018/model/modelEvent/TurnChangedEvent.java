package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectScheaCardFace;

/**
 * Event to inform that the turn has changed
 */
public class TurnChangedEvent extends ModelEvent {
    int round;

    public TurnChangedEvent(String emitter, String player, int round) {
        super(emitter, player);
        this.round = round;
    }

    public int getRound() {
        return round;
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

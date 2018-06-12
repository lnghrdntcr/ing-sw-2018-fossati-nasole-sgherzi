package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectScheaCardFace;

/**
 * Event that is used to inform that the DiceHolder has been changed
 */
public class DiceHolderChangedEvent extends ModelEvent {
    DiceHolderImmutable diceHolderImmutable;

    public DiceHolderChangedEvent(String emitter, String player, DiceHolderImmutable diceHolderImmutable) {
        super(emitter, player);
        this.diceHolderImmutable = diceHolderImmutable;
    }

    public DiceHolderImmutable getDiceHolderImmutable() {
        return diceHolderImmutable;
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

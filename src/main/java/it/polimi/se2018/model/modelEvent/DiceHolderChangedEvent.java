package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.utils.Event;

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
}

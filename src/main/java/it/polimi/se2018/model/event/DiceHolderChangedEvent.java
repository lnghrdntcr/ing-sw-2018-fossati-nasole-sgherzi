package it.polimi.se2018.model.event;

import it.polimi.se2018.model_view.DiceHolderImmutable;

public class DiceHolderChangedEvent extends Event {
    DiceHolderImmutable diceHolderImmutable;

    public DiceHolderChangedEvent(String emitter, String player, DiceHolderImmutable diceHolderImmutable) {
        super(emitter, player);
        this.diceHolderImmutable = diceHolderImmutable;
    }

    public DiceHolderImmutable getDiceHolderImmutable() {
        return diceHolderImmutable;
    }
}

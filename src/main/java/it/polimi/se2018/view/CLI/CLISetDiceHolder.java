package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.modelEvent.DiceHolderChangedEvent;
import it.polimi.se2018.utils.Event;

public class CLISetDiceHolder implements CLIState {
    @Override
    public CLIState doAction(CLI cli, Event e) {
        cli.setDiceHolder(((DiceHolderChangedEvent)e).getDiceHolderImmutable());
        return new CLISchemaChoice();
    }
}

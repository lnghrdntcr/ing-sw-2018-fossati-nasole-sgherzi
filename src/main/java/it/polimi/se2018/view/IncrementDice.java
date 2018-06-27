package it.polimi.se2018.view;

import it.polimi.se2018.view.CLI.CLIMainMenuState;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.CLI.State;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.viewEvent.ChangeDiceNumberEvent;

public abstract class IncrementDice extends State {
    int diceIndex;

    public IncrementDice(GameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex = diceIndex;
    }


    public void processIncrement(int increment) {
        if (increment == -1 && getGameTable().getDraftBoardImmutable().getDices()[diceIndex].getNumber() == 1) {
            throw new InputError("Cannot decrement a 1 dice!");
        }

        if (increment == 1 && getGameTable().getDraftBoardImmutable().getDices()[diceIndex].getNumber() == 6) {
            throw new InputError("Cannot increment a 6 dice!");
        }

        this.getGameTable().getView().sendEventToController(
                new ChangeDiceNumberEvent(
                        this.getClass().getName(),
                        "",
                        this.getGameTable().getView().getPlayer(),
                        this.getGameTable().getToolIndexByName("RoughingNipper"),
                        this.diceIndex,
                        increment
                ));

        getGameTable().setState(new CLIMainMenuState(this.getGameTable()));
    }

    public void processCancel(){
        getGameTable().setState(new CLIMainMenuState(this.getGameTable()));
    }
}

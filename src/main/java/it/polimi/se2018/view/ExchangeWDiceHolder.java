package it.polimi.se2018.view;

import it.polimi.se2018.view.CLI.CLIMainMenuState;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.CLI.State;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.viewEvent.SwapDiceFaceWithDiceHolderEvent;

import java.awt.*;

public abstract class ExchangeWDiceHolder extends State {

    private int diceIndex;

    public ExchangeWDiceHolder(GameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex = diceIndex;
    }

    protected void processVictim(Point victim) {

        if (victim == null){
            throw new InputError("Invalid Input");
        }

        getGameTable().getView().sendEventToController(
            new SwapDiceFaceWithDiceHolderEvent(
                getClass().getName(), "",
                getGameTable().getCurrentPlayer(),
                getGameTable().getToolIndexByName("CircularCutter"),
                diceIndex,
                victim.y,
                victim.x
            )
        );

        getGameTable().setState(new CLIMainMenuState(getGameTable()));
    }

    protected void processCancel(){
        getGameTable().setState(new CLIMainMenuState(this.getGameTable()));
    }


}

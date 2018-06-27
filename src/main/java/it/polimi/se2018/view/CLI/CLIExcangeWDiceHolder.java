package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.viewEvent.SwapDiceFaceWithDiceHolderEvent;

import java.awt.*;

public class CLIExcangeWDiceHolder extends State {

    int diceIndex;

    public CLIExcangeWDiceHolder(GameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex = diceIndex;
    }

    @Override
    public void process(String input) {

        if (input.equalsIgnoreCase("cancel")) getGameTable().setState(new CLIMainMenuState(this.getGameTable()));


        Point victim = CLIPrinter.decodePosition(input, getGameTable().getDiceHolderImmutable());

        if (victim == null) {
            CLIPrinter.printError("input invalid");
            getGameTable().setState(this);
        }


        getGameTable().getView().sendEventToController(new SwapDiceFaceWithDiceHolderEvent(getClass().getName(), "",
            getGameTable().getCurrentPlayer(), getGameTable().getToolIndexByName("CircularCutter"), diceIndex, victim.y,
            victim.x));

        getGameTable().setState(new CLIMainMenuState(getGameTable()));
    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose the dice you want to exchange:");
        CLIPrinter.printDiceHolder(getGameTable().getDiceHolderImmutable());
    }
}

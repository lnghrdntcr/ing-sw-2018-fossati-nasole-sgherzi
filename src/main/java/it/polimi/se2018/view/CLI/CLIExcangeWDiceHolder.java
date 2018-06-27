package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.viewEvent.SwapDiceFaceWithDiceHolderEvent;

import java.awt.*;

public class CLIExcangeWDiceHolder extends State {

    int diceIndex;
    public CLIExcangeWDiceHolder(CLIGameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex=diceIndex;
    }

    @Override
    public State process(String input) {

        if (input.equalsIgnoreCase("cancel")) return new CLIMainMenuState(this.getGameTable());


        Point victim = CLIPrinter.decodePosition(input, getGameTable().getDiceHolderImmutable());

        if(victim == null){
            CLIPrinter.printError("input invalid");
            return this;
        }


        getGameTable().getView().sendEventToController(new SwapDiceFaceWithDiceHolderEvent(getClass().getName(), "",
                getGameTable().getCurrentPlayer(), getGameTable().getToolIndexByName("CircularCutter"), diceIndex, victim.y,
                victim.x));

        return new CLIMainMenuState(getGameTable());
    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose the dice you want to exchange:");
        CLIPrinter.printDiceHolder(getGameTable().getDiceHolderImmutable());
    }
}

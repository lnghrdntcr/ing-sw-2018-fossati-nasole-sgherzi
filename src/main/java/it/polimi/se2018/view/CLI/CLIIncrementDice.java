package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.IncrementDice;
import it.polimi.se2018.view.InputError;
import it.polimi.se2018.view.viewEvent.ChangeDiceNumberEvent;

public class CLIIncrementDice extends IncrementDice {
    public CLIIncrementDice(GameTable gameTable, Integer diceIndex) {
        super(gameTable, diceIndex);
    }

    @Override
    public void process(String input) {

        if (input.equalsIgnoreCase("cancel")){
            getGameTable().setState(new CLIMainMenuState(this.getGameTable()));
            return;
        }

        if (!input.equalsIgnoreCase("d") && !input.equalsIgnoreCase("i")) {
            CLIPrinter.printError("Invalid choice");
            getGameTable().setState(this);
            return;
        }

        int increment = input.equalsIgnoreCase("d") ? -1 : 1;

        try{
            processIncrement(increment);
        }catch (InputError ie){
            CLIPrinter.printError(ie.getMessage());
            getGameTable().setState(this);
        }

    }

    @Override
    public void unrealize() {

    }

    //todo
    @Override
    public void render() {

        CLIPrinter.printQuestion("Insert [i] to increment or [d] to decrement, or cancel:");

    }
}

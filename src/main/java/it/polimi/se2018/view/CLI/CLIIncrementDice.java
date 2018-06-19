package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.viewEvent.ChangeDiceNumberEvent;

public class CLIIncrementDice extends State {
    int diceIndex;

    public CLIIncrementDice(CLIGameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex=diceIndex;
    }

    //todo
    @Override
    public State process(String input) {

        if (input.equalsIgnoreCase("cancel")) return new MainMenuState(this.getGameTable());

        if(!input.equalsIgnoreCase("d") && !input.equalsIgnoreCase("i")){
            CLIPrinter.printError("Invalid choice");
            return this;
        }

        this.getGameTable().getView().sendEventToController(
            new ChangeDiceNumberEvent(
                this.getClass().getName(),
                "",
                this.getGameTable().getView().getPlayer(),
                this.getGameTable().getToolIndexByName("RoughingNipper"),
                this.diceIndex,
                input.equalsIgnoreCase("d") ? -1 : 1
                ));

        return new MainMenuState(this.getGameTable());

    }

    //todo
    @Override
    public void render() {

        CLIPrinter.printQuestion("Insert [i] to increment or [d] to decrement, or cancel:");

    }
}

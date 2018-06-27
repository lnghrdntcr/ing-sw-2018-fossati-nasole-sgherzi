package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.viewEvent.ChangeDiceNumberEvent;

public class CLIIncrementDice extends State {
    int diceIndex;

    public CLIIncrementDice(CLIGameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex = diceIndex;
    }

    //todo
    @Override
    public void process(String input) {

        if (input.equalsIgnoreCase("cancel")) getGameTable().setState(new CLIMainMenuState(this.getGameTable()));

        if (!input.equalsIgnoreCase("d") && !input.equalsIgnoreCase("i")) {
            CLIPrinter.printError("Invalid choice");
            getGameTable().setState(this);
        }

        int increment = input.equalsIgnoreCase("d") ? -1 : 1;

        if (increment == -1 && getGameTable().getDraftBoardImmutable().getDices()[diceIndex].getNumber() == 1) {
            CLIPrinter.printError("Cannot decrement a 1 dice!");
            getGameTable().setState(this);
        }

        if (increment == 1 && getGameTable().getDraftBoardImmutable().getDices()[diceIndex].getNumber() == 6) {
            CLIPrinter.printError("Cannot increment a 6 dice!");
            getGameTable().setState(this);
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

    @Override
    public void unrealize() {

    }

    //todo
    @Override
    public void render() {

        CLIPrinter.printQuestion("Insert [i] to increment or [d] to decrement, or cancel:");

    }
}

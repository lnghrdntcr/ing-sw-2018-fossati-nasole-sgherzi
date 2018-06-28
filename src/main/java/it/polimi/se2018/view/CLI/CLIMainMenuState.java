package it.polimi.se2018.view.CLI;

import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.AbstractMainMenuState;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;

public class CLIMainMenuState extends AbstractMainMenuState {

    public CLIMainMenuState(GameTable gameTable) {
        super(gameTable);
    }

    @Override
    public void process(String input) {

        int selection = -1;

        Log.d("HANDLING PROCESSING OF INPUT" + input);

        try {
            selection = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            CLIPrinter.printError("The selection must be a number!");
            getGameTable().setState(this);
            return;
        }

        try {
            processSelection(selection);
        } catch (InputError ie) {
            CLIPrinter.printError(ie.getMessage());
            getGameTable().setState(this);
        }

    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {
        System.out.println();
        System.out.println("Round: " + getGameTable().getRoundNumber() + "\nTurn:" + (getGameTable().getRoundDirection() ? "1" : "2" +
                " "));
        CLIPrinter.printMenuLine(1, "View Draft Board");
        CLIPrinter.printMenuLine(2, "View Players");
        CLIPrinter.printMenuLine(3, "View Round Track");
        CLIPrinter.printMenuLine(4, "View Toolcards and Public Objectives");
        CLIPrinter.printMenuLine(5, "View your Table");
        if (this.getGameTable().isMyTurn()) {
            CLIPrinter.printMenuLine(6, "Place dice", !getGameTable().isDicePlaced());
            CLIPrinter.printMenuLine(7, "Use Toolcard", !getGameTable().isToolcardUsed());
            CLIPrinter.printMenuLine(8, "End Turn");
        }
    }
}

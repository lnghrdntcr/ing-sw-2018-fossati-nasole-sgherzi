package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.ChooseDice;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;

public class CLIChooseDice extends ChooseDice {
    public CLIChooseDice(GameTable gameTable, String toolName) {
        super(gameTable, toolName);
    }


    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose a dice: [0] to [" + getGameTable().getDraftBoardImmutable().getDices().length + "]");
        CLIPrinter.printDraftBoard(getGameTable().getDraftBoardImmutable());
    }

    @Override
    public void process(String input) {
        if (input.equalsIgnoreCase("cancel")){
            processCancel();
            return;
        }

        int dice = -1;

        try {
            dice = Integer.parseInt(input);
        } catch (RuntimeException ignored) {
            CLIPrinter.printError("Invalid input.");
            getGameTable().setState(this);
            return;
        }

        try {
            processDice(dice);
        } catch (InputError ie) {
            CLIPrinter.printError(ie.getMessage());
            getGameTable().setState(this);
            return;
        }
    }

    @Override
    public void unrealize() {

    }
}

package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.ChooseDice;
import it.polimi.se2018.view.InputError;

public class CLIChooseDice extends ChooseDice {
    public CLIChooseDice(CLIGameTable gameTable, String toolName) {
        super(gameTable, toolName);
    }


    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose a dice: [0] to [" + getGameTable().getDraftBoardImmutable().getDices().length + "]");
        CLIPrinter.printDraftBoard(getGameTable().getDraftBoardImmutable());
    }

    @Override
    public State process(String input) {
        if(input.equalsIgnoreCase("cancel")) return processCancel();

        int dice = -1;

        try {
            dice = Integer.parseInt(input);
        } catch (RuntimeException ignored){
            CLIPrinter.printError("Invalid input.");
            return this;
        }

        try {
            return processDice(dice);
        }catch (InputError ie){
            CLIPrinter.printError(ie.getMessage());
            return this;
        }
    }

    @Override
    public void unrealize() {

    }
}

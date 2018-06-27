package it.polimi.se2018.view.CLI;

import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import it.polimi.se2018.view.UseToolState;

public class CLIUseToolState extends UseToolState {

    public CLIUseToolState(GameTable gameTable) {
        super(gameTable);
    }

    @Override
    public void process(String input) {

        if (input.equalsIgnoreCase("cancel")) processCancel();

        int selection = -1;

        try {
            selection = Integer.parseInt(input);
        } catch (RuntimeException ex) {
            CLIPrinter.printError("Invalid input!");
            getGameTable().setState(this);
        }

        try {
            processUseToolCard(selection);
        } catch (InputError ie){
            CLIPrinter.printError(ie.getMessage());
        }

    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Select the tool card you want to use:");

        for (int i = 0; i < Settings.TOOLCARDS_N; i++) {
            CLIPrinter.printToolcard(getGameTable().getToolCardImmutable(i), i);
        }

        CLIPrinter.printQuestion("[0] to [" + (Settings.TOOLCARDS_N - 1) + "] or [cancel]");

    }
}

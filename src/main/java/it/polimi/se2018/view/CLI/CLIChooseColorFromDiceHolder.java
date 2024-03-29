package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.view.AbstractChooseColorFromDiceHolder;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;

public class CLIChooseColorFromDiceHolder extends AbstractChooseColorFromDiceHolder {

    public CLIChooseColorFromDiceHolder(GameTable gameTable, String toolName) {
        super(gameTable, toolName);
    }

    @Override
    public void process(String input) {
        if (input.equalsIgnoreCase("cancel")) {
            processCancel();
            return;
        }

        GameColor color = null;
        for (GameColor c : GameColor.values()) {
            if (c.toString().equalsIgnoreCase(input)) {
                color = c;
            }
        }

        if (color == null) {
            CLIPrinter.printError("Invalid choice");
            getGameTable().setState(this);
            return;
        }

        try {
            processColorSelected(color);
        }catch (InputError ie){
            CLIPrinter.printError(ie.getMessage());
            getGameTable().setState(this);
        }



    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Select a color:");
        for (GameColor c : GameColor.values()) {
            CLIPrinter.printQuestion(c.toString().toLowerCase());
        }
    }
}
package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.view.CLI.CLIGameTable;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.CLI.State;

public class CLIPlaceDice extends State {

    DiceFace diceFace;

    public CLIPlaceDice(CLIGameTable gameTable, DiceFace diceFace) {
        super(gameTable);
        this.diceFace=diceFace;
    }

    //todo
    @Override
    public State process(String input) {
        return null;
    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose the dice destination:");
        CLIPrinter.printSchema(getGameTable().getSchemas(getGameTable().getView().getPlayer()));
    }
}

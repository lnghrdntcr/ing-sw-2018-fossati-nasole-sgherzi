package it.polimi.se2018.view.gui;

import it.polimi.se2018.view.CLI.CLIGameTable;
import it.polimi.se2018.view.CLI.State;
import it.polimi.se2018.view.ChooseDice;

public class GUIChooseDice extends ChooseDice {
    public GUIChooseDice(CLIGameTable gameTable, String toolName) {
        super(gameTable, toolName);
    }

    @Override
    public void process(String input) {
        return;
    }

    @Override
    public void unrealize() {
        //TODO: chiudi finestra
    }

    @Override
    public void render() {
        //TODO: inizializza finestra
    }
}

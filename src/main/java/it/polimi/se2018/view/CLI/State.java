package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.Renderizable;

public abstract class State implements Renderizable {


    private final CLIGameTable gameTable;

    public State(CLIGameTable gameTable){
        this.gameTable = gameTable;
    }

    public CLIGameTable getGameTable() {
        return gameTable;
    }

    public abstract State process(String input);

}

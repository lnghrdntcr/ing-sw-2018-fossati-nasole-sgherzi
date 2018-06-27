package it.polimi.se2018.view;

import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.Renderizable;

public abstract class State implements Renderizable {


    private final GameTable gameTable;

    public State(GameTable gameTable){
        this.gameTable = gameTable;
    }

    public GameTable getGameTable() {
        return gameTable;
    }

    public abstract void process(String input);

    public abstract void unrealize();

}
